package com.zzt.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MicrPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrPayApplication.class, args);
    }

}
