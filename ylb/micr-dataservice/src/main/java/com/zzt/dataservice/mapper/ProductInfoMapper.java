package com.zzt.dataservice.mapper;

import com.zzt.api.model.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductInfoMapper {
    /**
     * 计算利率平均值
     *
     * @return
     */
    BigDecimal selectAvgRate();

    /**
     * 根据产品类型分页查询
     *
     * @param pType
     * @param offset
     * @param rows
     * @return
     */
    List<ProductInfo> selectByTypeLimit(@Param("pType") Integer pType, @Param("offset") Integer offset, @Param("rows") Integer rows);

    /**
     * 根据产品类型查询总记录数
     *
     * @param pType
     * @return
     */
    Integer selectCountByType(@Param("pType") Integer pType);

    /**
     * 根据投资更新产品剩余可投资金额
     *
     * @param productId
     * @param money
     * @return
     */
    int updateLeftProductMoneyByInvest(@Param("productId") Integer productId, @Param("money") BigDecimal money);

    /**
     * 产品已满标,更新产品满标状态
     *
     * @param productId
     * @return
     */
    int updateProductSellEnd(@Param("productId") Integer productId);

    /**
     * 计算起始时间到截止时间内已满标产品
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductInfo> selectFullTimeProducts(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 根据产品id更新产品状态
     *
     * @param id
     * @param newStatus
     * @return
     */
    int updateProductStatus(@Param("id") Integer id, @Param("newStatus") int newStatus);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);


}