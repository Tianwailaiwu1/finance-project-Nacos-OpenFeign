package com.zzt.front.pojo.recharge;

import com.zzt.api.model.RechargeRecord;
import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;

public class ResultView {
    private Integer id;
    private String result;
    private String rechargeDate;
    private BigDecimal rechargeMoney;

    public ResultView(RechargeRecord record) {
        this.id = record.getId();
        this.rechargeMoney = record.getRechargeMoney();

        if (record.getRechargeTime() != null) {
            rechargeDate = DateFormatUtils.format(record.getRechargeTime(), "yyyy-MM-dd");
        }

        switch (record.getRechargeStatus()) {
            case 0:
                result = "充值中";
            case 1:
                result = "充值成功";
            case 2:
                result = "充值失败";
        }
    }

    public Integer getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public String getRechargeDate() {
        return rechargeDate;
    }

    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }
}
