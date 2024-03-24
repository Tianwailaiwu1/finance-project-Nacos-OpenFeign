package com.zzt.pay.client;


import com.zzt.api.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Service
@FeignClient(name = "micr-dataservice",contextId = "userServicePay")
public interface UserServiceClient {
    @GetMapping("/dataservice/user/queryuser")
    User queryById(@RequestParam("uid") Integer uid);
}
