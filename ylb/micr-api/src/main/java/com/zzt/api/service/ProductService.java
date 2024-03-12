package com.zzt.api.service;

import com.zzt.api.model.ProductInfo;
import com.zzt.api.pojo.MultiProduct;

import java.util.List;

public interface ProductService {
    /**
     * 根据产品类型分页查询
     *
     * @param pType
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize);

    /**
     * 查询首页多个产品数据
     *
     * @return
     */
    MultiProduct queryIndexPageProducts();

    /**
     * 根据产品类型查询总记录数
     *
     * @param pType
     * @return
     */
    Integer queryRecordNumsByType(Integer pType);

    /**
     * 根据产品id查询产品信息
     *
     * @param id
     * @return
     */
    ProductInfo queryById(Integer id);
}
