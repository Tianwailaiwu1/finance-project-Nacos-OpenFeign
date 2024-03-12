package com.zzt.api.service;

import com.zzt.api.model.RechargeRecord;

import java.util.List;

public interface RechargeRecordService {
    /**
     * 根据用户id查询充值记录
     *
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<RechargeRecord> queryRechargeRecordById(Integer uid, Integer pageNo, Integer pageSize);

    /**
     * 新增充值记录
     *
     * @param record
     * @return
     */
    int addRechargeRecord(RechargeRecord record);

    /**
     * 返回支付结果校验
     *
     * @param orderId
     * @param payAmount
     * @param payResult
     * @return
     */
    int handleKqNotify(String orderId, String payAmount, String payResult);
}
