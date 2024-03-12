package com.zzt.dataservice.service.impl;

import com.zzt.api.model.RechargeRecord;
import com.zzt.api.service.RechargeRecordService;
import com.zzt.common.constant.RechargeRecordStatusConstant;
import com.zzt.common.util.CommonUtil;
import com.zzt.dataservice.mapper.FinanceAccountMapper;
import com.zzt.dataservice.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeRecordService.class, version = "1.0")
public class RechargeRecordServiceImpl implements RechargeRecordService {
    @Resource
    private RechargeRecordMapper rechargeRecordMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 根据用户id查询充值记录
     *
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<RechargeRecord> queryRechargeRecordById(Integer uid, Integer pageNo, Integer pageSize) {
        List<RechargeRecord> records = new ArrayList<>();
        //检验参数
        if (uid != null && uid > 0) {
            //分页数据校验
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            //调用mapper
            records = rechargeRecordMapper.selectRechargeRecordById(uid, offset, pageSize);
        }
        return records;
    }

    /**
     * 新增充值记录
     *
     * @param record
     * @return
     */
    @Override
    public int addRechargeRecord(RechargeRecord record) {
        return rechargeRecordMapper.insertSelective(record);
    }

    /**
     * 返回支付结果校验 result=0 订单号不存在  result=1 充值成功   result=3 该订单已经处理过了   result=4 充值记录与充值金额不一致  result=5 充值失败
     *
     * @param orderId
     * @param payAmount
     * @param payResult
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int handleKqNotify(String orderId, String payAmount, String payResult) {
        int result = 0;//订单号不存在
        int rows = 0;//标志更新条数
        RechargeRecord record = rechargeRecordMapper.selectByRechargeNo(orderId);
        //判断该订单号是否存在
        if (record == null) {
            return result;//该订单不存在
        }
        //判断充值状态
        if (record.getRechargeStatus() != 0) {
            //充值状态有误
            return result = 3;//该订单已经处理过了
        }
        //判断金额是否一致
        String rechargeMoney = record.getRechargeMoney().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
        if (!rechargeMoney.equals(payAmount)) {
            //金额不一致
            return result = 4;//充值记录与充值金额不一致
        }
        //判断返回的冲值是否成功状态
        if (!"10".equals(payResult)) {
            //充值失败
            return result = 5;//充值失败
        }
        //充值成功，更新用户的资金
        rows = financeAccountMapper.updateAvailableMoneyByRecharge(record.getUid(), record.getRechargeMoney());
        if (rows < 1) {
            throw new RuntimeException("更新充值用户资金余额失败");
        }
        //修改充值表的记录状态
        rows = rechargeRecordMapper.updateStatusByRecharge(record.getUid(), RechargeRecordStatusConstant.RECHARGE_STATUS_SUCCESS);
        if (rows < 1) {
            throw new RuntimeException("充值更新充值表记录状态失败");
        }
        result = 1;//充值成功
        return result;
    }
}
