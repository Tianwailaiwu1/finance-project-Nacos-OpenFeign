package com.zzt.dataservice.service.impl;

import com.zzt.api.model.ProductInfo;
import com.zzt.api.pojo.MultiProduct;
import com.zzt.api.service.ProductService;
import com.zzt.common.constant.ProductTypeConstant;
import com.zzt.common.util.CommonUtil;
import com.zzt.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = ProductService.class, version = "1.0")
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductInfoMapper productInfoMapper;

    /**
     * 根据产品类型分页查询
     *
     * @param pType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize) {
        List<ProductInfo> productInfos = new ArrayList<>();
        if (pType == 0 || pType == 1 || pType == 2) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            //计算从第几条数据开始
            int offset = (pageNo - 1) * pageSize;
            productInfos = productInfoMapper.selectByTypeLimit(pType, offset, pageSize);
        }
        return productInfos;
    }

    /**
     * 查询首页多个产品数据
     *
     * @return
     */
    @Override
    public MultiProduct queryIndexPageProducts() {
        MultiProduct multiProduct = new MultiProduct();
        //查询新手宝
        List<ProductInfo> xinShouBaoList = productInfoMapper.selectByTypeLimit(ProductTypeConstant.PRODUCT_TYPE_XINSHOUBAO, 0, 1);
        //查询优选
        List<ProductInfo> youXuanList = productInfoMapper.selectByTypeLimit(ProductTypeConstant.PRODUCT_TYPE_YOUXUAN, 0, 3);
        //查询散标
        List<ProductInfo> sanBiaoList = productInfoMapper.selectByTypeLimit(ProductTypeConstant.PRODUCT_TYPE_SANBIAO, 0, 3);
        //封装到MultiProduct中
        multiProduct.setXinShouBao(xinShouBaoList);
        multiProduct.setYouXuan(youXuanList);
        multiProduct.setSanBiao(sanBiaoList);
        return multiProduct;
    }

    /**
     * 根据产品类型查询总记录数
     *
     * @param pType
     * @return
     */
    @Override
    public Integer queryRecordNumsByType(Integer pType) {
        Integer counts = 0;
        if (pType == 0 || pType == 1 || pType == 2) {
            counts = productInfoMapper.selectCountByType(pType);
        }
        return counts;
    }

    /**
     * 根据产品id查询产品信息
     *
     * @param id
     * @return
     */
    @Override
    public ProductInfo queryById(Integer id) {
        ProductInfo productInfo = null;
        if (id != null && id > 0) {
            productInfo = productInfoMapper.selectByPrimaryKey(id);
        }
        return productInfo;
    }
}
