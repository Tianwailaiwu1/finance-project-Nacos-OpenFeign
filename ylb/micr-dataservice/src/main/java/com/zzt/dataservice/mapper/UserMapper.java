package com.zzt.dataservice.mapper;

import com.zzt.api.model.User;
import com.zzt.api.pojo.UserAccountInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     * 查询用户数量
     *
     * @return
     */
    int selectCountUser();

    /**
     * 根据手机号查询用户信息
     *
     * @param phone
     * @return
     */
    User selectByPhone(@Param("phone") String phone);

    /**
     * 添加记录,并获取新添加记录的主键值ID
     *
     * @param user
     * @return
     */
    int insertReturnPrimaryKey(User user);

    /**
     * 根据手机号和加密加盐之后的密码查询用户信息
     *
     * @param phone
     * @param hashPassword
     * @return
     */
    User selectLoginByPhoneAndHashPassword(@Param("phone") String phone, @Param("hashPassword") String hashPassword);

    /**
     * 根据手机号插入姓名与身份证号
     *
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    int updateRealName(@Param("phone") String phone, @Param("name") String name, @Param("idCard") String idCard);

    /**
     * 查询用户和资金信息
     *
     * @param uid
     * @return
     */
    UserAccountInfo selectUserAccountById(@Param("uid") Integer uid);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(@Param("uid") Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


}