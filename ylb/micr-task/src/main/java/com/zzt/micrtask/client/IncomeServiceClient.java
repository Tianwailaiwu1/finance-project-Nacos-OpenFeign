package com.zzt.micrtask.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@FeignClient(name = "micr-dataservice", contextId = "incomeService")
public interface IncomeServiceClient {
    /**
     * 生成收益计划
     */
    @GetMapping("/dataservice/income/generateincomeplan")
    void generateIncomePlan();

    /**
     * 收益返还
     */
    @GetMapping("/dataservice/income/generateincomeback")
    void generateIncomeBack();
}
