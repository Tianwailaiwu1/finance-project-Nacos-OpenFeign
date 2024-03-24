package com.zzt.micrtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling   //启动定时任务
@SpringBootApplication
public class MicrTaskApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MicrTaskApplication.class, args);
        TaskManager tm = (TaskManager) ctx.getBean("taskManager");
        tm.invokeGenerateIncomePlan();
    }

}
