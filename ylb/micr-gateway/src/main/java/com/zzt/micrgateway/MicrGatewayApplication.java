package com.zzt.micrgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicrGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrGatewayApplication.class, args);
    }

}
