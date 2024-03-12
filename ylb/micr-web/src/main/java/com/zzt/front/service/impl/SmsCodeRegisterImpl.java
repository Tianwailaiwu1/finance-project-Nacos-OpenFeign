package com.zzt.front.service.impl;

import com.zzt.common.constant.RedisKey;
import com.zzt.front.SmsUtils.HttpUtils;
import com.zzt.front.config.AliyunSmsConfig;
import com.zzt.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 注册发送短信验证码
 */
@Service
public class SmsCodeRegisterImpl implements SmsService {
    @Resource
    private AliyunSmsConfig aliyunSmsConfig;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @return
     */
    @Override
    public boolean sendSms(String phone) {
        //生成随机的四位数验证码
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println(random);

        //将验证码存入redis
        cacheSmsCode(phone, random);

        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + aliyunSmsConfig.getAppcode());
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:" + random);
        bodys.put("template_id", "CST_ptdie100");  //该模板为调试接口专用，短信下发有受限制，调试成功后请联系客服报备专属模板
        bodys.put("phone_number", phone);


        try {
            HttpResponse response = HttpUtils.doPost(aliyunSmsConfig.getHost(), aliyunSmsConfig.getPath(), aliyunSmsConfig.getMethod(), headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 验证短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    @Override
    public boolean checkSmsCode(String phone, String code) {
        String key = RedisKey.KEY_USER_PHONE + phone;
        if (redisTemplate.hasKey(key)) {
            //从redis中查询对应key的值
            String querySmsCode = redisTemplate.opsForValue().get(key);
            //获取的值与接收来的验证码比较
            if (code.equals(querySmsCode)) {
                //验证码正确
                return true;
            }
        }
        return false;
    }

    //将数据放入redis中
    private void cacheSmsCode(String phone, String code) {
        String key = RedisKey.KEY_USER_PHONE + phone;
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, code);
        // 设置过期时间，设置为 5 分钟
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    }

}

