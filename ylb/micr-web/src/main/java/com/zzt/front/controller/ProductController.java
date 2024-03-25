package com.zzt.front.controller;

import com.zzt.api.model.ProductInfo;
import com.zzt.api.pojo.BidInfoProduct;
import com.zzt.api.pojo.MultiProduct;
import com.zzt.common.enums.RCode;
import com.zzt.common.util.CommonUtil;
import com.zzt.front.pojo.PageInfo;
import com.zzt.front.pojo.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "理财产品功能")
@RestController
@RequestMapping("/v1")
public class ProductController extends BaseController {
    /**
     * 查询首页产品信息
     *
     * @return
     */
    @ApiOperation(value = "首页三类产品列表", notes = "一个新手宝，三个优选，三个散标")
    @GetMapping("/product/index")
    public RespResult queryProductIndex() {
        RespResult respResult = RespResult.ok();
        MultiProduct multiProduct = productServiceClient.queryIndexPageProducts();
        respResult.setData(multiProduct);
        return respResult;
    }

    /**
     * 按产品类型分页查询
     *
     * @param pType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "产品分页", notes = "将三类产品分页显示并提供分页信息")
    @GetMapping("/product/list")
    public RespResult queryProductByType(@RequestParam("pType") Integer pType, @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (pType != null && (pType == 0 || pType == 1 || pType == 2)) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            //分页处理，记录总条数
            Integer recordNums = productServiceClient.queryRecordNumsByType(pType);
            if (recordNums > 0) {
                //产品集合
                List<ProductInfo> productInfos = productServiceClient.queryByTypeLimit(pType, pageNo, pageSize);
                result = RespResult.ok();
                result.setList(productInfos);
                //分页信息
                PageInfo pageInfo = new PageInfo(pageNo, pageSize, recordNums);
                result.setPageInfo(pageInfo);
            }
        } else {
            //产品类型有误
            result.setCode(RCode.REQUEST_PRODUCT_TYPE_ERR);
        }
        return result;
    }

    /**
     * 查询某个产品的详情以及5条投资记录
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "产品详情", notes = "查询某个产品的详情以及5条投资记录")
    @GetMapping("/product/info")
    public RespResult queryProductDetail(@RequestParam("productId") Integer id) {
        RespResult result = RespResult.fail();
        if (id != null && id > 0) {
            //查询产品详情
            ProductInfo productInfo = productServiceClient.queryById(id);
            if (productInfo != null) {
                //查询该产品的投资记录(5条)
                List<BidInfoProduct> bidInfoProductList = investServiceClient.queryBidListByProductId(id, 1, 5);
                //查询成功
                result = RespResult.ok();
                //包装产品详情
                result.setData(productInfo);
                //包装产品投资记录集合
                result.setList(bidInfoProductList);
            } else {
                //查询失败
                result.setCode(RCode.PRODUCT_OFFLINE);
            }
        }
        return result;
    }
}
