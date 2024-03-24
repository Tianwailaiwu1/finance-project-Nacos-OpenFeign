package com.zzt.dataservice.controller;

import com.zzt.api.service.IncomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/income")
public class IncomeController {
    @Resource
    private IncomeService incomeService;

    /**
     * 生成收益计划
     */
    @GetMapping("/generateincomeplan")
    public void generateIncomePlan() {
        incomeService.generateIncomePlan();
    }

    /**
     * 收益返还
     */
    @GetMapping("/generateincomeback")
    public void generateIncomeBack(){
        incomeService.generateIncomeBack();
    }
}
