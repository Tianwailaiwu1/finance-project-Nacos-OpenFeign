package com.zzt.api.service;

import com.zzt.api.model.User;
import com.zzt.api.pojo.UserAccountInfo;

public interface UserService {
    /**
     * 根据手机号查询用户数据
     *
     * @param phone 手机号
     * @return
     */
    User queryByPhone(String phone);

    /**
     * 用户注册
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return
     */
    int userRegister(String phone, String pwd);

    /**
     * 用户登录
     *
     * @param phone
     * @param pwd
     * @return
     */
    User userLogin(String phone, String pwd);

    /**
     * 更新实名认证后的信息
     *
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    boolean modifyRealName(String phone, String name, String idCard);

    /**
     * 查询用户和资金信息
     *
     * @param uid
     * @return
     */
    UserAccountInfo queryUserAllInfo(Integer uid);

    /**
     * 根据用户id查询用户信息
     *
     * @param uid
     * @return
     */
    User queryById(Integer uid);
}
