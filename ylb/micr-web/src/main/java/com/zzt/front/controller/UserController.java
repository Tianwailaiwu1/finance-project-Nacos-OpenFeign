package com.zzt.front.controller;

import com.zzt.api.model.User;
import com.zzt.api.pojo.UserAccountInfo;
import com.zzt.common.constant.RedisKey;
import com.zzt.common.enums.RCode;
import com.zzt.common.util.CommonUtil;
import com.zzt.common.util.JwtUtil;
import com.zzt.front.pojo.RespResult;
import com.zzt.front.service.RealNameService;
import com.zzt.front.service.SmsService;
import com.zzt.front.dto.RealNameDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户功能")
@RestController
@RequestMapping("/v1")
public class UserController extends BaseController {

    @Resource
    private RedisTemplate<String, User> redisTemplate;
    @Resource
    private RealNameService realNameService;
    @Resource
    private SmsService smsService;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 验证手机号是否存在
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "验证手机号是否可注册", notes = "在注册功能中验证手机号是否被注册过，没有被注册过的则可以被注册")
    @GetMapping("/user/phone/exists")
    public RespResult phoneExists(@RequestParam("phone") String phone) {
        RespResult result = RespResult.fail();
        result.setCode(RCode.PHONE_EXISTS);
        if (CommonUtil.checkPhone(phone)) {

            // 先尝试从 Redis 中获取用户信息
            User cachedUser = getCachedUser(phone);
            if (cachedUser != null) {
                return result; // 如果在 Redis 中找到了用户信息，直接返回手机号已经被注册
            }

            // Redis 中不存在，查询数据库
            User user = userServiceClient.queryByPhone(phone);
            if (user == null) {
                //表示手机号没有被注册过，可以被注册
                result = RespResult.ok();
            } else {
                // 将查询结果存入 Redis，设置过期时间，防止数据过期不一致
                cacheUser(phone, user);
            }
        } else {
            //手机格式不正确
            result.setCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;

    }

    /**
     * 手机号注册用户
     *
     * @param phone
     * @param pwd
     * @param scode
     * @return
     */
    @ApiOperation(value = "注册用户", notes = "根据手机号注册用户，并同时开通资金账户")
    @PostMapping("/user/register")
    public RespResult userRegister(@RequestParam("phone") String phone, @RequestParam("pwd") String pwd, @RequestParam("scode") String scode) {
        RespResult result = RespResult.fail();

        // 检查手机号格式是否正确
        if (!CommonUtil.checkPhone(phone)) {
            result.setCode(RCode.PHONE_FORMAT_ERR);
            return result;
        }

        // 检查密码格式是否正确
        if (!CommonUtil.checkPwd(pwd)) {
            result.setCode(RCode.REQUEST_PARAM_ERR);
            return result;
        }

        // 验证短信验证码
        if (!smsService.checkSmsCode(phone, scode)) {
            result.setCode(RCode.SMS_CODE_INVALID);
            return result;
        }

        // 执行用户注册
        int registerResult = userServiceClient.userRegister(phone, pwd);
        if (registerResult == 1) {
            // 注册成功，返回成功信息
            return RespResult.ok();
        } else if (registerResult == 2) {
            // 手机号已存在
            result.setCode(RCode.PHONE_EXISTS);
        } else {
            // 注册失败
            result.setCode(RCode.REQUEST_PARAM_ERR);
        }

        return result;
    }

    /**
     * 用户登录
     *
     * @param phone
     * @param pwd
     * @param scode
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "验证手机号密码进行用户登录，服务器返回token，浏览器保存在localStorage")
    @PostMapping("/user/login")
    public RespResult userLogin(@RequestParam("phone") String phone, @RequestParam("pwd") String pwd, @RequestParam("scode") String scode) throws Exception {
        RespResult result = RespResult.fail();
        // 检查手机号格式是否正确
        if (!CommonUtil.checkPhone(phone)) {
            result.setCode(RCode.PHONE_FORMAT_ERR);
            return result;
        }

        // 检查密码格式是否正确
        if (!CommonUtil.checkPwd(pwd)) {
            result.setCode(RCode.REQUEST_PARAM_ERR);
            return result;
        }

        // 验证短信验证码
        if (!smsService.checkSmsCode(phone, scode)) {
            result.setCode(RCode.SMS_CODE_INVALID);
            return result;
        }

        //参数正确后，执行用户登录逻辑
        User user = userServiceClient.userLogin(phone, pwd);
        if (user != null) {
            //登录成功,生成token
            Map<String, Object> data = new HashMap<>();
            data.put("uid", user.getId());
            String jwtToken = jwtUtil.createJwt(data, 120);

            //返回成功信息
            result = RespResult.ok();
            //将jwtToken返回
            result.setAccessToken(jwtToken);

            //返回部分用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("uid", user.getId());
            userInfo.put("phone", user.getPhone());
            userInfo.put("name", user.getName());
            result.setData(userInfo);
        } else {
            //查询不到该手机号密码对应的用户信息
            result.setCode(RCode.PHONE_PASSWORD_LOGIN_INVALID);
        }
        return result;
    }

    /**
     * 用户实名认证
     *
     * @param realNameDto
     * @return
     */
    @ApiOperation(value = "用户实名认证", notes = "提供手机号姓名和身份证号，验证姓名和身份证号是否一致")
    @PostMapping("/user/realname")
    public RespResult userRealName(@RequestBody RealNameDto realNameDto) {
        RespResult result = RespResult.fail();
        // 检查手机号格式是否正确
        if (!CommonUtil.checkPhone(realNameDto.getPhone())) {
            result.setCode(RCode.PHONE_FORMAT_ERR);
            return result;
        }
        //检查该手机号是否已经实名认证过了
        if (!realNameService.checkIsRealName(realNameDto.getPhone())) {
            result.setCode(RCode.PHONE_REALNAME_EXISTS);
            return result;
        }
        //检查姓名和身份证号是否为空
        if (!StringUtils.isNotBlank(realNameDto.getName()) && !StringUtils.isNotBlank(realNameDto.getIdCard())) {
            result.setCode(RCode.REQUEST_PARAM_ERR);
            return result;
        }
        // 验证短信验证码
        if (!smsService.checkSmsCode(realNameDto.getPhone(), realNameDto.getCode())) {
            result.setCode(RCode.SMS_CODE_INVALID);
            return result;
        }
        //检查身份证号是否是合法格式
        if (!CommonUtil.checkIdCard(realNameDto.getIdCard())) {
            result.setCode(RCode.IDCARD_INVALID);
            return result;
        }
        //调用第三方实名认证接口，判断姓名与身份证号是否一致
        boolean realNameResult = realNameService.handleRealName(realNameDto.getPhone(), realNameDto.getName(), realNameDto.getIdCard());
        if (realNameResult) {
            //实名认证通过
            result = RespResult.ok();
        } else {
            //实名认证失败
            result.setCode(RCode.REALNAME_FAIL);
        }
        return result;
    }


    /**
     * 用户中心
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "用户中心", notes = "根据用户id获取相关的用户信息展示在用户中心页面")
    @GetMapping("/user/usercenter")
    public RespResult userCenter(@RequestHeader(value = "uid", required = false) Integer uid) {
        RespResult result = RespResult.fail();
        //验证参数,uid
        if (uid != null && uid > 0) {
            //查询用户信息
            UserAccountInfo userAccountInfo = userServiceClient.queryUserAllInfo(uid);
            //判断用户信息是否为空
            if (userAccountInfo == null) {
                //用户信息不存在
                result.setCode(RCode.USER_NOT_EXISTS);
                return result;
            }
            result = RespResult.ok();
            Map<String, Object> data = new HashMap<>();
            data.put("name", userAccountInfo.getName());
            data.put("phone", userAccountInfo.getPhone());
            data.put("headerImage", userAccountInfo.getHeaderImage());
            data.put("availableMoney", userAccountInfo.getAvailableMoney());
            data.put("lastLoginTime", DateFormatUtils.format(userAccountInfo.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss"));
            result.setData(data);
        }
        return result;
    }

    //从redis中获取数据
    private User getCachedUser(String phone) {
        String key = RedisKey.KEY_USER_PHONE + phone;
        User cachedUser = redisTemplate.opsForValue().get(key);
        if (cachedUser != null) {
            return cachedUser;
        }
        // 如果从 Redis 中获取的用户数据为 null，则返回 null
        return null;
    }

    //将数据放入redis中
    private void cacheUser(String phone, User user) {
        String key = RedisKey.KEY_USER_PHONE + phone;
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        operations.set(key, user);
        // 设置过期时间，设置为 1 小时
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
    }
}
