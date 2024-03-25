package com.zzt.front.controller;

import com.zzt.common.enums.RCode;
import com.zzt.common.util.CommonUtil;
import com.zzt.front.pojo.RespResult;
import com.zzt.front.service.impl.SmsCodeRegisterImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "短信业务")
@RestController
@RequestMapping("/v1")
public class SmsController extends BaseController {
    @Resource
    private SmsCodeRegisterImpl smsCodeRegister;

    /**
     * 发送注册短信验证码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "注册短信验证码发送",notes = "向已经验证过的手机号发送短信验证码")
    @GetMapping("/sms/code/register")
    public RespResult sendCodeRegister(@RequestParam("phone") String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            //调用service方法发送注册短信验证码
            boolean isSend = smsCodeRegister.sendSms(phone);
            if (isSend) {
                //发送成功
                result = RespResult.ok();
            } else {
                //发送失败
                result.setCode(RCode.SMSCODE_SEND_ERR);
            }
        } else {
            //手机格式不正确
            result.setCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }


}
