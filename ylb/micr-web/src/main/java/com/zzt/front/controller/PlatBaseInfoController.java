package com.zzt.front.controller;

import com.zzt.api.pojo.BaseInfo;
import com.zzt.common.constant.RedisKey;
import com.zzt.front.pojo.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Api(tags = "平台基本信息")
@RestController
@RequestMapping("/v1")
public class PlatBaseInfoController extends BaseController {
    @Resource
    private RedisTemplate<String, BaseInfo> redisTemplate;

    /**
     * 平台基本信息
     */
    @ApiOperation(value = "平台基本三项数据", notes = "注册人数，平均利率，总投资金额")
    @GetMapping("/plat/info")
    public RespResult queryPlatBaseInfo() {
        //先尝试从redis中获取数据
        BaseInfo cachedPlatInfo = getCachedPlatInfo();
        if (cachedPlatInfo != null) {
            RespResult respResult = RespResult.ok();
            respResult.setData(cachedPlatInfo);
            return respResult;
        }
        // Redis 中不存在，查询数据库,调用远程服务,并放入redis中
        BaseInfo baseInfo = platBaseInfoServiceClient.queryPlatBaseInfo();
        RespResult respResult = RespResult.ok();
        respResult.setData(baseInfo);
        //将平台数据信息放入redis
        cachePlatInfo(baseInfo);
        return respResult;
    }

    //从redis中获取数据
    private BaseInfo getCachedPlatInfo() {
        String key = RedisKey.KEY_PLAT_INFO;
        return redisTemplate.opsForValue().get(key);
    }

    //将数据放入redis中
    private void cachePlatInfo(BaseInfo baseInfo) {
        String key = RedisKey.KEY_PLAT_INFO;
        ValueOperations<String, BaseInfo> operations = redisTemplate.opsForValue();
        operations.set(key, baseInfo);
        // 设置过期时间，设置为 1 分钟
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);
    }
}
