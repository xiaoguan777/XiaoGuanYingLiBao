package com.xiaoguan.api.pojo;

import com.xiaoguan.api.model.User;

import java.math.BigDecimal;

public class UserAccountInfo extends User {
    private BigDecimal availableMoney;

    public BigDecimal getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(BigDecimal availableMoney) {
        this.availableMoney = availableMoney;
    }
}
