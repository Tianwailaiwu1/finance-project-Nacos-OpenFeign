package com.zzt.common.constant;

/**
 * Redis的KEY
 */
public class RedisKey {
    /**
     * 投资排行榜
     */
    public static final String KEY_INVEST_RANK = "invest:rank";
    /**
     * 用户手机号
     */
    public static final String KEY_USER_PHONE = "user:phone";
    /**
     * 平台基本三项信息
     */
    public static final String KEY_PLAT_INFO = "plat:info";
    /**
     * 充值订单号redis的key
     */
    public static final String KEY_RECHARGE_ORDERID = "recharge:orderid:seq";
    /**
     * orderId(订单号)
     */
    public static final String RECHARGE_ORDERID_SET = "recharge:orderid:set";
}
