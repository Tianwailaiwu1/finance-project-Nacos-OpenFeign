package com.zzt.dataservice.controller;

import com.zzt.api.model.ProductInfo;
import com.zzt.api.pojo.MultiProduct;
import com.zzt.api.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;

    /**
     * 根据产品类型分页查询
     *
     * @param pType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/querybytypelimit")
    public List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize) {
        return productService.queryByTypeLimit(pType, pageNo, pageSize);
    }

    /**
     * 查询首页多个产品数据
     *
     * @return
     */
    @GetMapping("/queryindexpageproducts")
    public MultiProduct queryIndexPageProducts() {
        return productService.queryIndexPageProducts();
    }

    /**
     * 根据产品类型查询总记录数
     *
     * @param pType
     * @return
     */
    @GetMapping("/queryrecordnumsbytype")
    public Integer queryRecordNumsByType(Integer pType) {
        return productService.queryRecordNumsByType(pType);
    }

    /**
     * 根据产品id查询产品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/querybyid")
    public ProductInfo queryById(Integer id) {
        return productService.queryById(id);
    }
}
