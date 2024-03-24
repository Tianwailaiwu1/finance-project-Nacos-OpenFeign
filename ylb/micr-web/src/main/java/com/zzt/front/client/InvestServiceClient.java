package com.zzt.front.client;

import com.zzt.api.pojo.BidInfoProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Service
@FeignClient(name = "micr-dataservice", contextId = "investService")
public interface InvestServiceClient {
    /**
     * 查询某个产品的投资记录
     *
     * @param productId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/dataservice/invest/querybidlistbyproductid")
    List<BidInfoProduct> queryBidListByProductId(@RequestParam("productId") Integer productId, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    /**
     * 购买理财产品，更新投资排行榜信息      投资失败:0     投资成功:1      资金账户不存在:2      资金不足:3   理财产品不存在:4   购买金额不符合条件:5     购买的理财产品已满标:6
     *
     * @param uid
     * @param productId
     * @param money
     * @return
     */
    @GetMapping("/dataservice/invest/investproduct")
    int investProduct(@RequestParam("uid") Integer uid, @RequestParam("productId") Integer productId, @RequestParam("money") BigDecimal money);
}
