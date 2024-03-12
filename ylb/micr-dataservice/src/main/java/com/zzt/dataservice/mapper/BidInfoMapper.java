package com.zzt.dataservice.mapper;

import com.zzt.api.model.BidInfo;
import com.zzt.api.pojo.BidInfoProduct;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {
    /**
     * 累计成交金额
     *
     * @return
     */
    BigDecimal selectSumBidMoney();

    /**
     * 某个产品投资信息
     *
     * @param productId
     * @param offset
     * @param rows
     * @return
     */
    List<BidInfoProduct> selectByProductId(@Param("productId") Integer productId, @Param("offset") int offset, @Param("rows") Integer rows);

    /**
     * 根据产品id查询该产品的多个投资记录
     *
     * @param id
     * @return
     */
    List<BidInfo> selectBidByProductId(@Param("productId") Integer productId);

    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);


}