package com.zzt.dataservice.controller;

import com.zzt.api.model.RechargeRecord;
import com.zzt.api.service.RechargeRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/rechargerecord")
public class RechargeRecordController {
    @Resource
    private RechargeRecordService rechargeRecordService;

    /**
     * 根据用户id查询充值记录
     *
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/queryrechargerecordbyid")
    public List<RechargeRecord> queryRechargeRecordById(Integer uid, Integer pageNo, Integer pageSize){
        return rechargeRecordService.queryRechargeRecordById(uid,pageNo,pageSize);
    }
    /**
     * 新增充值记录
     *
     * @param record
     * @return
     */
    @PostMapping("/addrechargerecord")
    public int addRechargeRecord(@RequestBody RechargeRecord record){
        return rechargeRecordService.addRechargeRecord(record);
    }

    /**
     * 返回支付结果校验 result=0 订单号不存在  result=1 充值成功   result=3 该订单已经处理过了   result=4 充值记录与充值金额不一致  result=5 充值失败
     *
     * @param orderId
     * @param payAmount
     * @param payResult
     * @return
     */
    @GetMapping("/handlekqnotify")
    public int handleKqNotify(String orderId, String payAmount, String payResult){
        return rechargeRecordService.handleKqNotify(orderId,payAmount,payResult);
    }

}
