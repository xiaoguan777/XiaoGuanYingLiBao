package com.xiaoguan.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class InvestInfo implements Serializable {
    private Integer id;

    private Integer prodId;

    private Integer uid;

    private BigDecimal bidMoney;

    private String bidTime;

    public String getBidTime() {
        return bidTime;
    }

    private Integer bidStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public BigDecimal getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(BigDecimal bidMoney) {
        this.bidMoney = bidMoney;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }

    public Integer getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(Integer bidStatus) {
        this.bidStatus = bidStatus;
    }
}