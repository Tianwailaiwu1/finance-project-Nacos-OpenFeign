package com.zzt.dataservice.service.impl;

import com.zzt.api.model.BidInfo;
import com.zzt.api.model.FinanceAccount;
import com.zzt.api.model.ProductInfo;
import com.zzt.api.pojo.BidInfoProduct;
import com.zzt.api.service.InvestService;
import com.zzt.common.constant.ProductBidStatusConstant;
import com.zzt.common.constant.ProductStatusConstant;
import com.zzt.common.util.CommonUtil;
import com.zzt.dataservice.mapper.BidInfoMapper;
import com.zzt.dataservice.mapper.FinanceAccountMapper;
import com.zzt.dataservice.mapper.ProductInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class InvestServiceImpl implements InvestService {
    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;

    /**
     * 查询某个产品的投资记录
     *
     * @param productId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<BidInfoProduct> queryBidListByProductId(Integer productId, Integer pageNo, Integer pageSize) {
        List<BidInfoProduct> bidInfoProductList = new ArrayList<>();
        if (productId != null && productId > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            bidInfoProductList = bidInfoMapper.selectByProductId(productId, offset, pageSize);
        }
        return bidInfoProductList;
    }

    /**
     * 购买理财产品，更新投资排行榜信息      投资失败:0     投资成功:1      资金账户不存在:2      资金不足:3   理财产品不存在:4   购买金额不符合条件:5     购买的理财产品已满标:6
     *
     * @param uid
     * @param productId
     * @param money
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int investProduct(Integer uid, Integer productId, BigDecimal money) {
        int result = 0; //标志投资状态信息
        int rows = 0; //标志投资资金账户更新信息
        //检验参数
        if (uid == null || uid <= 0 || productId == null || productId <= 0 || money == null || money.intValue() % 100 != 0 || money.intValue() < 100) {
            //参数不正确，投资失败
            return result;
        }
        //查询账户金额是否足够
        FinanceAccount account = financeAccountMapper.selectAccountByUidForUpdate(uid);
        //判断是否存在该资金账户
        if (account == null) {
            //资金账户不存在
            result = 2;
            return result;
        }
        if (!CommonUtil.ge(account.getAvailableMoney(), money)) {
            //资金余额不足
            result = 3;
            return result;
        }
        //查看产品是否可以购买
        ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
        if (productInfo == null) {
            //理财产品不存在
            result = 4;
            return result;
        }
        //判断理财产品购买是否符合条件
        if (CommonUtil.ge(money, productInfo.getLeftProductMoney()) || CommonUtil.ge(productInfo.getBidMinLimit(), money) || CommonUtil.ge(money, productInfo.getBidMaxLimit())) {
            //购买金额不符合条件
            result = 5;
            return result;
        }
        //判断要购买的理财产品是否处于可售状态
        if (productInfo.getProductStatus() != ProductStatusConstant.PRODUCT_STATUS_SELLING) {
            //购买的理财产品已满标
            result = 6;
            return result;
        }
        //可以进行购买了,更新资金账户余额
        rows = financeAccountMapper.updateAvailableMoneyByInvest(uid, money);
        if (rows < 1) {
            //更新账户资金失败
            throw new RuntimeException("更新资金账户异常");
        }
        //更新理财产品剩余可投资金额
        rows = productInfoMapper.updateLeftProductMoneyByInvest(productId, money);
        if (rows < 1) {
            //更新产品剩余可投资金额失败
            throw new RuntimeException("更新产品剩余可投资金额异常");
        }

        //创建投资记录
        BidInfo bidInfo = new BidInfo();
        bidInfo.setBidMoney(money);
        bidInfo.setBidStatus(ProductBidStatusConstant.INVEST_STATUS_SUCCESS);
        bidInfo.setBidTime(new Date());
        bidInfo.setProdId(productId);
        bidInfo.setUid(uid);
        bidInfoMapper.insertSelective(bidInfo);

        //判断产品是否已经售完，更新产品可售状态
        ProductInfo dbProductInfo = productInfoMapper.selectByPrimaryKey(productId);
        if (dbProductInfo.getLeftProductMoney().compareTo(new BigDecimal("0")) == 0) {
            //产品已满标,更新产品满标状态
            rows = productInfoMapper.updateProductSellEnd(productId);
            if (rows < 1) {
                //更新产品满标状态失败
                throw new RuntimeException("更新产品满标状态异常");
            }
        }

        //最后投资成功
        result = 1;
        return result;
    }
}
