package com.zzt.front.service;

public interface SmsService {
    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @return
     */
    boolean sendSms(String phone);

    /**
     * 验证短信验证码是否正确
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    boolean checkSmsCode(String phone, String code);
}
