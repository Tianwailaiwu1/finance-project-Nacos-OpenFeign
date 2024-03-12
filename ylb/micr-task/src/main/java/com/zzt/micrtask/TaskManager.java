package com.zzt.micrtask;

import com.zzt.api.service.IncomeService;
import com.zzt.common.util.HttpClientUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定义方法，表示要执行定时任务的功能
 */
@Component("taskManager")
public class TaskManager {
    @DubboReference(interfaceClass = IncomeService.class, version = "1.0")
    private IncomeService incomeService;

    /**
     * 生成收益计划
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void invokeGenerateIncomePlan() {
        incomeService.generateIncomePlan();
    }

    /**
     * 收益返还
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void invokeGenerateIncomeBack() {
        incomeService.generateIncomeBack();
    }

    /**
     * 补单接口
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    public void invokeKqQuery(){
        try {
            String url = "http://localhost:9000/pay/kq/receive/query";
            HttpClientUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
