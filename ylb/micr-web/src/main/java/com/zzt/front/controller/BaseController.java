package com.zzt.front.controller;

import com.zzt.api.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
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
    @DubboReference(interfaceClass = PlatBaseInfoService.class, version = "1.0")
    protected PlatBaseInfoService platBaseInfoService;
    /**
     * 产品服务
     */
    @DubboReference(interfaceClass = ProductService.class, version = "1.0")
    protected ProductService productService;
    /**
     * 产品详情服务
     */
    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    protected InvestService investService;
    /**
     * 用户信息服务
     */
    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    protected UserService userService;
    /**
     * 充值服务
     */
    @DubboReference(interfaceClass = RechargeRecordService.class, version = "1.0")
    protected RechargeRecordService rechargeRecordService;
}
