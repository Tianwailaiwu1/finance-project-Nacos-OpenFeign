package com.zzt.dataservice.controller;

import com.zzt.api.pojo.BaseInfo;
import com.zzt.api.service.PlatBaseInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/platbaseinfo")
public class PlatBaseInfoController {
    @Resource
    private PlatBaseInfoService platBaseInfoService;

    /**
     * 计算利率,注册人数,累计成交金额
     *
     * @return
     */
    @GetMapping("/queryplatbaseinfo")
    public BaseInfo queryPlatBaseInfo(){
        return platBaseInfoService.queryPlatBaseInfo();
    }
}
