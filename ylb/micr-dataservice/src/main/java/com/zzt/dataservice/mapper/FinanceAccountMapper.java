package com.zzt.dataservice.mapper;

import com.zzt.api.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {

    /**
     * 根据uid查询账户资金，利用ForUpdate进行查询后更新
     *
     * @param uid
     * @return
     */
    FinanceAccount selectAccountByUidForUpdate(@Param("uid") Integer uid);

    /**
     * 根据投资更新资金账户余额
     *
     * @param uid
     * @param money
     * @return
     */
    int updateAvailableMoneyByInvest(@Param("uid") Integer uid, @Param("money") BigDecimal money);

    /**
     * 根据收益返还更新用户资金余额
     *
     * @param uid
     * @param bidMoney
     * @param incomeMoney
     * @return
     */
    int updateAvailableMoneyByIncomeBack(@Param("uid") Integer uid, @Param("bidMoney") BigDecimal bidMoney, @Param("incomeMoney") BigDecimal incomeMoney);

    /**
     * 根据充值更新用户资金余额
     *
     * @param uid
     * @param rechargeMoney
     * @return
     */
    int updateAvailableMoneyByRecharge(@Param("uid") Integer uid, @Param("rechargeMoney") BigDecimal rechargeMoney);


    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);


}