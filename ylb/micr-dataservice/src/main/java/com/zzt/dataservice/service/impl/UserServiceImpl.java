package com.zzt.dataservice.service.impl;

import com.zzt.api.model.FinanceAccount;
import com.zzt.api.model.User;
import com.zzt.api.pojo.UserAccountInfo;
import com.zzt.api.service.UserService;
import com.zzt.common.util.CommonUtil;
import com.zzt.dataservice.mapper.FinanceAccountMapper;
import com.zzt.dataservice.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return
     */
    @Override
    public User queryByPhone(String phone) {
        User user = null;
        user = userMapper.selectByPhone(phone);
        return user;
    }

    /**
     * 使用手机号密码进行用户注册
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return result:0 失败     result:1 成功  result:2 手机号已存在
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
    //开启事务设置了事务隔离级别为可重复读，保证插入数据后立马查询该数据的id主键值
    @Override
    public int userRegister(String phone, String pwd) {
        int result = 0;
        int insertedId;
        //检查手机号在数据库中是否存在
        User queryUser = userMapper.selectByPhone(phone);
        if (queryUser == null) {
            //检查手机号，密码格式是否正确
            if (CommonUtil.checkPhone(phone) && CommonUtil.checkPwd(pwd)) {
                //可以注册
                //对密码进行md5加密与加盐
                String salt = CommonUtil.generateSalt();
                String hashPassword = CommonUtil.hashPassword(pwd, salt);

                User user = new User();
                user.setRandomSalt(salt);
                user.setPhone(phone);
                user.setLoginPassword(hashPassword);
                user.setAddTime(new Date());
                //插入用户数据
                userMapper.insertReturnPrimaryKey(user);
                //获取插入的主键值
                insertedId = user.getId();

                //对新增的用户进行开通资金账户表
                FinanceAccount account = new FinanceAccount();
                account.setUid(insertedId);
                account.setAvailableMoney(new BigDecimal("0"));
                financeAccountMapper.insertSelective(account);

                //成功返回result = 1
                result = 1;
            }
        } else {
            //手机号已存在，result = 2
            result = 2;
        }

        return result;
    }

    /**
     * 使用手机号密码进行用户登录
     *
     * @param phone
     * @param pwd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User userLogin(String phone, String pwd) {
        User user = null;
        if (CommonUtil.checkPhone(phone) && CommonUtil.checkPwd(pwd)) {
            //获取该手机号使用的盐值,用于比较输入的密码与注册的密码是否一致
            User queryUser = userMapper.selectByPhone(phone);
            //查询不到该手机号对应的用户信息
            if (queryUser == null) {
                return null;
            }
            String hashPassword = CommonUtil.hashPassword(pwd, queryUser.getRandomSalt());
            //根据手机号和加密加盐之后的密码查询用户信息
            user = userMapper.selectLoginByPhoneAndHashPassword(phone, hashPassword);
            //更新用户表中最后的登录时间
            if (user != null) {
                user.setLastLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
            }
        }
        return user;
    }

    /**
     * 更新实名认证后的信息
     *
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    @Override
    public boolean modifyRealName(String phone, String name, String idCard) {
        int rows = 0;
        //验证参数是否为空
        if (!StringUtils.isAnyBlank(phone, name, idCard)) {
            //参数正确，向数据库插入数据
            rows = userMapper.updateRealName(phone, name, idCard);
        }
        return rows > 0;
    }

    /**
     * 查询用户和资金信息
     *
     * @param uid
     * @return
     */
    @Override
    public UserAccountInfo queryUserAllInfo(Integer uid) {
        UserAccountInfo info = null;
        if (uid != null && uid > 0) {
            //查询数据库
            info = userMapper.selectUserAccountById(uid);
        }
        return info;
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param uid
     * @return
     */
    @Override
    public User queryById(Integer uid) {
        User user = null;
        if (uid != null && uid > 0) {
            user = userMapper.selectByPrimaryKey(uid);
        }
        return user;
    }
}
