package com.xiaoguan.api.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class BaseInfo implements Serializable {
    /**收益率平均值*/
    private BigDecimal HistoryAvgRate;
    /**累计成交金额*/
    private BigDecimal sumBidMoney;
    /**注册人数*/
    private Integer registerUsers;

    @Override
    public String toString() {
        return "BaseInfo{" +
                "HistoryAvgRate=" + HistoryAvgRate +
                ", sumBidMoney=" + sumBidMoney +
                ", registerUsers=" + registerUsers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseInfo baseInfo = (BaseInfo) o;
        return Objects.equals(getHistoryAvgRate(), baseInfo.getHistoryAvgRate()) && Objects.equals(getSumBidMoney(), baseInfo.getSumBidMoney()) && Objects.equals(getRegisterUsers(), baseInfo.getRegisterUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHistoryAvgRate(), getSumBidMoney(), getRegisterUsers());
    }

    public BigDecimal getHistoryAvgRate() {
        return HistoryAvgRate;
    }

    public void setHistoryAvgRate(BigDecimal historyAvgRate) {
        HistoryAvgRate = historyAvgRate;
    }

    public BigDecimal getSumBidMoney() {
        return sumBidMoney;
    }

    public void setSumBidMoney(BigDecimal sumBidMoney) {
        this.sumBidMoney = sumBidMoney;
    }

    public Integer getRegisterUsers() {
        return registerUsers;
    }

    public void setRegisterUsers(Integer registerUsers) {
        this.registerUsers = registerUsers;
    }

    public BaseInfo(BigDecimal historyAvgRate, BigDecimal sumBidMoney, Integer registerUsers) {
        HistoryAvgRate = historyAvgRate;
        this.sumBidMoney = sumBidMoney;
        this.registerUsers = registerUsers;
    }

    public BaseInfo() {
    }
}
