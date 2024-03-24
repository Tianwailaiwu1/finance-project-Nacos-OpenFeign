package com.zzt.pay.client;

import com.zzt.api.model.RechargeRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(name = "micr-dataservice", contextId = "rechargeRecordService")
public interface RechargeRecordClient {
    /**
     * 新增充值记录
     *
     * @param record
     * @return
     */
    @PostMapping("/dataservice/rechargerecord/addrechargerecord")
    int addRechargeRecord(@RequestBody RechargeRecord record);

    /**
     * 返回支付结果校验 result=0 订单号不存在  result=1 充值成功   result=3 该订单已经处理过了   result=4 充值记录与充值金额不一致  result=5 充值失败
     *
     * @param orderId
     * @param payAmount
     * @param payResult
     * @return
     */
    @GetMapping("/dataservice/rechargerecord/handlekqnotify")
    int handleKqNotify(@RequestParam("orderId") String orderId, @RequestParam("payAmount") String payAmount, @RequestParam("payResult") String payResult);
}
