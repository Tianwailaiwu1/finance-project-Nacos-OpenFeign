package com.zzt.dataservice.service.impl;

import com.zzt.api.pojo.BaseInfo;
import com.zzt.api.service.PlatBaseInfoService;
import com.zzt.dataservice.mapper.BidInfoMapper;
import com.zzt.dataservice.mapper.ProductInfoMapper;
import com.zzt.dataservice.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
@Service
public class PlatBaseInfoServiceImpl implements PlatBaseInfoService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private BidInfoMapper bidInfoMapper;

    /**
     * 计算利率,注册人数,累计成交金额
     *
     * @return
     */
    @Override
    public BaseInfo queryPlatBaseInfo() {
        //获取注册人数
        int countUser = userMapper.selectCountUser();
        //计算利率
        BigDecimal avgRate = productInfoMapper.selectAvgRate();
        //累计成交金额
        BigDecimal sumBidMoney = bidInfoMapper.selectSumBidMoney();
        //封装对象
        BaseInfo baseInfo = new BaseInfo(avgRate, sumBidMoney, countUser);
        return baseInfo;
    }
}
