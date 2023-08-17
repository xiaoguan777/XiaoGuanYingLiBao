package com.xiaoguan.api.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class BidInfoProduct implements Serializable {
    private Integer id;
    private String tuoMinPhone;
    private String bidTime;
    private BigDecimal bidMoney;

    @Override
    public String toString() {
        return "BidInfoProduct{" +
                "id=" + id +
                ", tuoMinPhone='" + tuoMinPhone + '\'' +
                ", bidTime='" + bidTime + '\'' +
                ", bidMoney=" + bidMoney +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BidInfoProduct that = (BidInfoProduct) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTuoMinPhone(), that.getTuoMinPhone()) && Objects.equals(getBidTime(), that.getBidTime()) && Objects.equals(getBidMoney(), that.getBidMoney());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTuoMinPhone(), getBidTime(), getBidMoney());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTuoMinPhone() {
        return tuoMinPhone;
    }

    public void setTuoMinPhone(String tuoMinPhone) {
        this.tuoMinPhone = tuoMinPhone;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }

    public BigDecimal getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(BigDecimal bidMoney) {
        this.bidMoney = bidMoney;
    }

    public BidInfoProduct(Integer id, String tuoMinPhone, String bidTime, BigDecimal bidMoney) {
        this.id = id;
        this.tuoMinPhone = tuoMinPhone;
        this.bidTime = bidTime;
        this.bidMoney = bidMoney;
    }

    public BidInfoProduct() {
    }
}
