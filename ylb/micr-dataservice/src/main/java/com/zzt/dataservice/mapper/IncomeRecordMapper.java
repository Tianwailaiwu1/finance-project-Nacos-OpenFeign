package com.zzt.dataservice.mapper;

import com.zzt.api.model.IncomeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IncomeRecordMapper {
    /**
     * 获取到期的收益记录
     * @param expiredDate
     * @return
     */
    List<IncomeRecord> selectExpiredIncome(@Param("expiredDate") Date expiredDate);
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);


}