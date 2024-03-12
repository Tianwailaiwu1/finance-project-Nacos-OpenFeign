package com.zzt.front.pojo;

import com.zzt.common.enums.RCode;

import java.util.List;

/**
 * 统一的应答结果类
 */
public class RespResult {
    //应答码
    private int code;
    //应答信息
    private String msg;
    //单个数据
    private Object data;
    //集合数据
    private List list;
    //分页
    private PageInfo pageInfo;
    //jwt-token
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    //表示成功的RespResult对象
    public static RespResult ok() {
        RespResult result = new RespResult();
        result.setCode(RCode.SUCCESS);
        return result;
    }

    //表示失败的RespResult对象
    public static RespResult fail() {
        RespResult result = new RespResult();
        result.setCode(RCode.UNKNOW);
        return result;
    }


    public void setCode(RCode rCode) {
        this.code = rCode.getCode();
        this.msg = rCode.getText();
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
