package com.zzt.front.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
public class SwaggerConfigurationSettings {
        @Bean
        public Docket docket() {
            Docket docket = new Docket(DocumentationType.SWAGGER_2);
            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title("理财宝项目")
                    .version("1.0")
                    .description("SpringBoot+Vue的微服务金融项目")
                    .contact(new Contact("朱祖涛", "http://www.zzt.com", "2274240412@qq.com")).build();
            docket = docket.apiInfo(apiInfo);
            //指定controller包
            docket = docket.select().apis(RequestHandlerSelectors.basePackage("com.zzt.front.controller")).build();
            return docket;
        }

}
