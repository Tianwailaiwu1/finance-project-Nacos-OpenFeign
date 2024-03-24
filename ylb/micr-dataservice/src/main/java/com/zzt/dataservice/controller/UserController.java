package com.zzt.dataservice.controller;

import com.zzt.api.model.User;
import com.zzt.api.pojo.UserAccountInfo;
import com.zzt.api.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return
     */
    @GetMapping("/querybyphone")
    public User queryByPhone(String phone) {
        return userService.queryByPhone(phone);
    }

    /**
     * 使用手机号密码进行用户注册
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return result:0 失败     result:1 成功  result:2 手机号已存在
     */
    @GetMapping("/userregister")
    public int userRegister(String phone, String pwd) {
        return userService.userRegister(phone, pwd);
    }

    /**
     * 使用手机号密码进行用户登录
     *
     * @param phone
     * @param pwd
     * @return
     */
    @GetMapping("/userlogin")
    public User userLogin(String phone, String pwd) {
        return userService.userLogin(phone, pwd);
    }

    /**
     * 更新实名认证后的信息
     *
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    @GetMapping("/modifyrealname")
    public boolean modifyRealName(String phone, String name, String idCard) {
        return userService.modifyRealName(phone, name, idCard);
    }

    /**
     * 查询用户和资金信息
     *
     * @param uid
     * @return
     */
    @GetMapping("/queryuserallinfo")
    public UserAccountInfo queryUserAllInfo(Integer uid) {
        return userService.queryUserAllInfo(uid);
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param uid
     * @return
     */
    @GetMapping("/queryuser")
    public User queryById(Integer uid) {
        return userService.queryById(uid);
    }
}
