package com.zzt.front.client;

import com.zzt.api.pojo.BaseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@FeignClient(name = "micr-dataservice", contextId = "platBaseInfoService")
public interface PlatBaseInfoServiceClient {
    /**
     * 计算利率,注册人数,累计成交金额
     *
     * @return
     */
    @GetMapping("/dataservice/platbaseinfo/queryplatbaseinfo")
    BaseInfo queryPlatBaseInfo();
}
