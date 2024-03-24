package com.zzt.front.service.impl;

import com.zzt.api.model.User;
import com.zzt.front.SmsUtils.HttpUtils;
import com.zzt.front.client.UserServiceClient;
import com.zzt.front.config.AliyunRealNameConfig;
import com.zzt.front.service.RealNameService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class RealNameServiceImpl implements RealNameService {
    @Resource
    private AliyunRealNameConfig aliyunRealNameConfig;
    @Resource
    private UserServiceClient userServiceClient;

    /**
     * 实名认证
     *
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    @Override
    public boolean handleRealName(String phone, String name, String idCard) {
        boolean result = false;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + aliyunRealNameConfig.getAppcode());
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("id_number", idCard);
        bodys.put("name", name);


        try {
            HttpResponse response = HttpUtils.doPost(aliyunRealNameConfig.getHost(), aliyunRealNameConfig.getPath(), aliyunRealNameConfig.getMethod(), headers, querys, bodys);
            //System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            //将姓名与身份证号更新进数据库
            result = userServiceClient.modifyRealName(phone, name, idCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 检查该手机号是否已经实名认证 true:没有实名认证过  false:已经实名认证了
     *
     * @param phone
     * @return
     */
    @Override
    public boolean checkIsRealName(String phone) {
        //向数据库查询该手机号的姓名与身份证号
        User user = userServiceClient.queryByPhone(phone);
        //验证该手机号查出来的用户信息中的姓名与身份证号是否为空
        if (StringUtils.isNotBlank(user.getName()) && StringUtils.isNotBlank(user.getIdCard())) {
            //姓名和身份证号都不为空,该手机号已经实名认证了
            return false;
        }
        //都为空则没有实名认证过
        return true;
    }
}
