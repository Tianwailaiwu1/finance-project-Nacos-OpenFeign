package com.zzt.front.client;

import com.zzt.api.model.User;
import com.zzt.api.pojo.UserAccountInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(name = "micr-dataservice", contextId = "userServiceWeb")
public interface UserServiceClient {
    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return
     */
    @GetMapping("/dataservice/user/querybyphone")
    User queryByPhone(@RequestParam("phone") String phone);

    /**
     * 使用手机号密码进行用户注册
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return result:0 失败     result:1 成功  result:2 手机号已存在
     */
    @GetMapping("/dataservice/user/userregister")
    int userRegister(@RequestParam("phone") String phone, @RequestParam("pwd") String pwd);

    /**
     * 使用手机号密码进行用户登录
     *
     * @param phone
     * @param pwd
     * @return
     */
    @GetMapping("/dataservice/user/userlogin")
    User userLogin(@RequestParam("phone") String phone, @RequestParam("pwd") String pwd);

    /**
     * 更新实名认证后的信息
     *
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    @GetMapping("/dataservice/user/modifyrealname")
    boolean modifyRealName(@RequestParam("phone") String phone, @RequestParam("name") String name, @RequestParam("idCard") String idCard);

    /**
     * 查询用户和资金信息
     *
     * @param uid
     * @return
     */
    @GetMapping("/dataservice/user/queryuserallinfo")
    UserAccountInfo queryUserAllInfo(@RequestParam("uid") Integer uid);

    /**
     * 根据用户id查询用户信息
     *
     * @param uid
     * @return
     */
    @GetMapping("/dataservice/user/queryuser")
    User queryById(@RequestParam("uid") Integer uid);
}
