package com.zzt.dataservice.controller;

import com.zzt.api.pojo.BidInfoProduct;
import com.zzt.api.service.InvestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/invest")
public class InvestController {
    @Resource
    private InvestService investService;

    /**
     * 查询某个产品的投资记录
     *
     * @param productId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/querybidlistbyproductid")
    public List<BidInfoProduct> queryBidListByProductId(Integer productId, Integer pageNo, Integer pageSize){
        return investService.queryBidListByProductId(productId,pageNo,pageSize);
    }

    /**
     * 购买理财产品，更新投资排行榜信息      投资失败:0     投资成功:1      资金账户不存在:2      资金不足:3   理财产品不存在:4   购买金额不符合条件:5     购买的理财产品已满标:6
     *
     * @param uid
     * @param productId
     * @param money
     * @return
     */
    @GetMapping("/investproduct")
    public int investProduct(Integer uid, Integer productId, BigDecimal money){
        return investService.investProduct(uid,productId,money);
    }
}
