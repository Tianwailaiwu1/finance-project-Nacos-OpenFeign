package com.zzt.dataservice.mapper;

import com.zzt.api.model.RechargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeRecordMapper {

    /**
     * 根据用户id查询充值记录
     *
     * @param uid
     * @param offset
     * @param rows
     * @return
     */
    List<RechargeRecord> selectRechargeRecordById(@Param("uid") Integer uid, @Param("offset") Integer offset, @Param("rows") Integer rows);

    /**
     * 根据充值订单号查询充值记录
     *
     * @param orderId
     * @return
     */
    RechargeRecord selectByRechargeNo(@Param("orderId") String orderId);

    /**
     * 根据充值更新充值记录状态
     *
     * @param uid
     * @param rechargeStatusSuccess
     * @return
     */
    int updateStatusByRecharge(@Param("uid") Integer uid, @Param("newStatus") int rechargeStatusSuccess);

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);


}