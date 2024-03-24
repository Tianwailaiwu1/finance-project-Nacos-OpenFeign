package com.zzt.front.controller;

import com.zzt.front.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * 声明公共的方法，属性等
 */
public class BaseController {
    /**
     * Redis服务
     */
    @Resource
    protected StringRedisTemplate stringRedisTemplate;
    ;
    /**
     * 平台基本信息服务
     */
    @Autowired
    protected PlatBaseInfoServiceClient platBaseInfoServiceClient;
    /**
     * 产品服务
     */
    @Autowired
    protected ProductServiceClient productServiceClient;
    /**
     * 产品详情服务
     */
    @Autowired
    protected InvestServiceClient investServiceClient;
    /**
     * 用户信息服务
     */
    @Autowired
    protected UserServiceClient userServiceClient;
    /**
     * 充值服务
     */
    @Autowired
    protected RechargeRecordServiceClient rechargeRecordServiceClient;
}
