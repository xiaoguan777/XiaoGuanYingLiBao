package com.xiaoguan.front.view.invest;

import java.io.Serializable;
import java.util.Objects;
//用于存储柚子排行榜的数据

public class RankView implements Serializable {
    private String phone;//手机号
    private Double money;//金额

    @Override
    public String toString() {
        return "RankView{" +
                "phone='" + phone + '\'' +
                ", money=" + money +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankView rankView = (RankView) o;
        return Objects.equals(getPhone(), rankView.getPhone()) && Objects.equals(getMoney(), rankView.getMoney());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhone(), getMoney());
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public RankView(String phone, Double money) {
        this.phone = phone;
        this.money = money;
    }

    public RankView() {
    }
}
