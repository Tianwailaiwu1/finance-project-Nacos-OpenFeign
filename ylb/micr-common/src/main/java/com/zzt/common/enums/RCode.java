package com.zzt.common.enums;

/**
 * 返回状态码与状态信息枚举类
 */
public enum RCode {
    UNKNOW(0, "请稍后重试"),
    SUCCESS(1000, "请求成功"),
    REQUEST_PARAM_ERR(1001, "请求参数有误"),
    REQUEST_PRODUCT_TYPE_ERR(1002, "产品类型有误"),
    PRODUCT_OFFLINE(1003, "产品不存在"),
    PHONE_FORMAT_ERR(1004, "手机格式不正确"),
    PHONE_EXISTS(1005, "手机号已经被注册了"),
    SMSCODE_SEND_ERR(1006, "短信验证码发送失败"),
    SMS_CODE_INVALID(1007, "验证码无效"),
    PHONE_PASSWORD_LOGIN_INVALID(1008, "手机号或密码错误"),
    IDCARD_INVALID(1009, "身份证号不合法"),
    REALNAME_FAIL(1010, "实名认证失败"),
    PHONE_REALNAME_EXISTS(1011, "该手机号已经实名认证"),

    USER_NOT_EXISTS(1012, "用户不存在"),
    TOKEN_INVALID(3000, "token无效");


    /**
     * 应答码
     * 0:默认
     * 1000-2000是请求参数有误，逻辑问题
     * 2000-3000是服务器请求错误
     * 3000-4000是访问dubbo的应答结果
     */
    private int code;
    private String text;

    RCode(int c, String t) {
        this.code = c;
        this.text = t;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
