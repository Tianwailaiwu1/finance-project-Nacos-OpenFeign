package com.zzt.front.pojo.invest;

import java.math.BigDecimal;

/**
 * 投资排行榜
 */
public class RankView {
    //手机号
    private String phone;
    //投资金额
    private BigDecimal money;

    public RankView() {
    }

    public RankView(String phone, BigDecimal money) {
        this.phone = phone;
        this.money = money;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
