package com.zzt.front;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.zzt.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//启用Swagger服务
@EnableSwagger2
@EnableSwaggerBootstrapUI
//启用事务
@EnableTransactionManagement
@EnableFeignClients
public class MicrWebApplication {

    //创建JwtUtil工具类
    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public JwtUtil jwtUtil() {
        JwtUtil jwtUtil = new JwtUtil(secretKey);
        return jwtUtil;
    }

    public static void main(String[] args) {
        SpringApplication.run(MicrWebApplication.class, args);
    }

}

