package com.zzt.micrtask;

import com.zzt.common.util.HttpClientUtils;
import com.zzt.micrtask.client.IncomeServiceClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定义方法，表示要执行定时任务的功能
 */
@Component("taskManager")
public class TaskManager {
    @Resource
    private IncomeServiceClient incomeServiceClient;

    /**
     * 生成收益计划
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void invokeGenerateIncomePlan() {
        incomeServiceClient.generateIncomePlan();
    }

    /**
     * 收益返还
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void invokeGenerateIncomeBack() {
        incomeServiceClient.generateIncomeBack();
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
