package com.xiaoguan.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RechargeRecord implements Serializable {
    private Integer id;

    private Integer uid;

    private String rechargeNo;

    private Integer rechargeStatus;
    private String rechargeStatusDetail;

    private BigDecimal rechargeMoney;

    private String rechargeTime;

    private String rechargeDesc;

    private String channel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getRechargeNo() {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo;
    }

    public void setRechargeStatusDetail(String rechargeStatusDetail) {
        this.rechargeStatusDetail = rechargeStatusDetail;
    }

    public String getRechargeStatusDetail() {
        return rechargeStatusDetail;
    }

    public Integer getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(Integer rechargeStatus) {
        switch (rechargeStatus){
            case 0:
                this.setRechargeStatusDetail("充值中");
                break;
            case 1:
                this.setRechargeStatusDetail("充值成功");
                break;
            default:
                this.setRechargeStatusDetail("充值失败");
        }
        this.rechargeStatus = rechargeStatus;
    }

    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(BigDecimal rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getRechargeTime() {

        return rechargeTime;
    }

    public void setRechargeTime(String rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public String getRechargeDesc() {
        return rechargeDesc;
    }

    public void setRechargeDesc(String rechargeDesc) {
        this.rechargeDesc = rechargeDesc;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}