package com.zzt.api.service;

import com.zzt.api.model.ProductInfo;
import com.zzt.api.pojo.BaseInfo;

import java.util.List;

public interface PlatBaseInfoService {
    /**
     * 计算利率,注册人数,累计成交金额
     *
     * @return
     */
    BaseInfo queryPlatBaseInfo();
}
