package com.zzt.pay.service;

import com.zzt.api.model.User;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

public interface KuaiQianService {
    /**
     * 根据用户id获取用户信息
     *
     * @param uid
     * @return
     */
    User queryUser(Integer uid);

    /**
     * 生成快钱支付接口的数据
     *
     * @param uid
     * @param rechargeMoney
     */
    Map<String, String> generateFormData(Integer uid, String phone, BigDecimal rechargeMoney);

    /**
     * 创建充值记录
     *
     * @param uid
     * @param rechargeMoney
     * @param orderId
     * @return
     */
    boolean addRecharge(Integer uid, BigDecimal rechargeMoney, String orderId);

    /**
     * 将订单号存入redis
     *
     * @param orderId
     */
    void addOrderIdToRedis(String orderId);

    /**
     * 处理异步返回通知
     *
     * @param request
     */
    void enCodeByCer(HttpServletRequest request);

    /**
     * 调用快钱的查询接口
     */
    void handleQueryOrder();
}
