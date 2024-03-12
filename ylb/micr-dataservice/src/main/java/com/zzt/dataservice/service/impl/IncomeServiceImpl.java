package com.zzt.dataservice.service.impl;

import com.zzt.api.model.BidInfo;
import com.zzt.api.model.IncomeRecord;
import com.zzt.api.model.ProductInfo;
import com.zzt.api.service.IncomeService;
import com.zzt.api.service.InvestService;
import com.zzt.common.constant.IncomeStatusConstant;
import com.zzt.common.constant.ProductStatusConstant;
import com.zzt.common.constant.ProductTypeConstant;
import com.zzt.dataservice.mapper.BidInfoMapper;
import com.zzt.dataservice.mapper.FinanceAccountMapper;
import com.zzt.dataservice.mapper.IncomeRecordMapper;
import com.zzt.dataservice.mapper.ProductInfoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class, version = "1.0")
public class IncomeServiceImpl implements IncomeService {
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private IncomeRecordMapper incomeRecordMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 生成收益计划
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void generateIncomePlan() {
        //获取要处理的理财产品记录
        //计算当前时间前一天凌晨已满标产品
        Date currentDate = new Date();
        Date beginTime = DateUtils.truncate(DateUtils.addDays(currentDate, -1), Calendar.DATE);
        Date endTime = DateUtils.truncate(currentDate, Calendar.DATE);
        List<ProductInfo> productInfoList = productInfoMapper.selectFullTimeProducts(beginTime, endTime);

        //获取每个理财产品的多个投资记录
        //标志更新数据条数
        int rows = 0;
        BigDecimal income = null;
        //日利率
        BigDecimal dayRate = null;
        //周期
        BigDecimal cycle = null;
        //到期时间
        Date incomeDate = null;
        for (ProductInfo product : productInfoList) {
            //计算日利率
            dayRate = product.getRate().divide(new BigDecimal("360"), 10, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("100"), 100, RoundingMode.HALF_UP);

            //根据产品类型不同 计算不同周期
            if (product.getProductType() == ProductTypeConstant.PRODUCT_TYPE_XINSHOUBAO) {
                //周期为天的
                cycle = new BigDecimal(product.getCycle());
                //计算产品到期时间
                incomeDate = DateUtils.addDays(product.getProductFullTime(), (1 + product.getCycle()));
            } else {
                //周期为月的
                cycle = new BigDecimal(product.getCycle() * 30);
                incomeDate = DateUtils.addDays(product.getProductFullTime(), (1 + product.getCycle() * 30));
            }

            List<BidInfo> bidInfoList = bidInfoMapper.selectBidByProductId(product.getId());
            //计算每笔投资的   利息  和 到期时间
            for (BidInfo bid : bidInfoList) {
                //利息 = 本金 * 周期 * 利率
                income = bid.getBidMoney().multiply(cycle).multiply(dayRate);
                //创建收益记录
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setBidId(bid.getId());
                incomeRecord.setBidMoney(bid.getBidMoney());
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeStatus(IncomeStatusConstant.INCOME_STATUS_PLAN);
                incomeRecord.setProdId(product.getId());
                incomeRecord.setIncomeMoney(income);
                incomeRecord.setUid(bid.getUid());
                incomeRecordMapper.insertSelective(incomeRecord);
            }

            //更新产品状态
            rows = productInfoMapper.updateProductStatus(product.getId(), ProductStatusConstant.PRODUCT_STATUS_PLAN);
            if (rows < 1) {
                throw new RuntimeException("生成收益，更新产品状态失败");
            }
        }
    }

    /**
     * 收益返还
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void generateIncomeBack() {
        //获取要处理的到期的收益记录
        Date curDate = new Date();
        Date expiredDate = DateUtils.truncate(DateUtils.addDays(curDate, -1), Calendar.DATE);
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectExpiredIncome(expiredDate);

        int rows = 0;
        //给对应用户实现收益返还  本金 + 利息
        for (IncomeRecord ir : incomeRecordList) {
            rows = financeAccountMapper.updateAvailableMoneyByIncomeBack(ir.getUid(), ir.getBidMoney(), ir.getIncomeMoney());
            if (rows < 1) {
                throw new RuntimeException("收益返还，更新用户资金账户失败");
            }
            //更新收益表状态为1，表示已经返还
            ir.setIncomeStatus(IncomeStatusConstant.INCOME_STATUS_BACK);
            rows = incomeRecordMapper.updateByPrimaryKey(ir);
            if (rows < 1) {
                throw new RuntimeException("更新收益表状态已返还失败");
            }
        }
    }
}
