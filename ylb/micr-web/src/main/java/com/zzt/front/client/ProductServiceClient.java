package com.zzt.front.client;

import com.zzt.api.model.ProductInfo;
import com.zzt.api.pojo.MultiProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(name = "micr-dataservice", contextId = "productService")
public interface ProductServiceClient {
    /**
     * 根据产品类型分页查询
     *
     * @param pType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/dataservice/product/querybytypelimit")
    List<ProductInfo> queryByTypeLimit(@RequestParam("pType") Integer pType, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    /**
     * 查询首页多个产品数据
     *
     * @return
     */
    @GetMapping("/dataservice/product/queryindexpageproducts")
    MultiProduct queryIndexPageProducts();

    /**
     * 根据产品类型查询总记录数
     *
     * @param pType
     * @return
     */
    @GetMapping("/dataservice/product/queryrecordnumsbytype")
    Integer queryRecordNumsByType(@RequestParam("pType") Integer pType);

    /**
     * 根据产品id查询产品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/dataservice/product/querybyid")
    ProductInfo queryById(@RequestParam("id") Integer id);
}
