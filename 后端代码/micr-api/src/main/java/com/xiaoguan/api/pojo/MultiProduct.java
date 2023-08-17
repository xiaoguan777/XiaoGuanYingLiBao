package com.xiaoguan.api.pojo;

import com.xiaoguan.api.model.ProductInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 多个产品的数据
 */
public class MultiProduct implements Serializable {
    private List<ProductInfo> xinShouBao;
    private List<ProductInfo> youXuan;
    private List<ProductInfo> sanBiao;

    public MultiProduct(List<ProductInfo> xinShouBao, List<ProductInfo> youXuan, List<ProductInfo> sanBiao) {
        this.xinShouBao = xinShouBao;
        this.youXuan = youXuan;
        this.sanBiao = sanBiao;
    }

    public MultiProduct() {
    }

    @Override
    public String toString() {
        return "MultiProduct{" +
                "xinShouBao=" + xinShouBao +
                ", youXuan=" + youXuan +
                ", sanBiao=" + sanBiao +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiProduct that = (MultiProduct) o;
        return Objects.equals(getXinShouBao(), that.getXinShouBao()) && Objects.equals(getYouXuan(), that.getYouXuan()) && Objects.equals(getSanBiao(), that.getSanBiao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getXinShouBao(), getYouXuan(), getSanBiao());
    }

    public List<ProductInfo> getXinShouBao() {
        return xinShouBao;
    }

    public void setXinShouBao(List<ProductInfo> xinShouBao) {
        this.xinShouBao = xinShouBao;
    }

    public List<ProductInfo> getYouXuan() {
        return youXuan;
    }

    public void setYouXuan(List<ProductInfo> youXuan) {
        this.youXuan = youXuan;
    }

    public List<ProductInfo> getSanBiao() {
        return sanBiao;
    }

    public void setSanBiao(List<ProductInfo> sanBiao) {
        this.sanBiao = sanBiao;
    }
}
