package com.xiaoguan.common.constant;

public class RedisKey {
    /*投资排行榜*/
    public static final String KEY_INVEST_RANK="INVEST:RANK";
    /*注册时短信验证码*/
    public static final String KEY_SMS_CODE_REG="SMS:REG:";
    /*登录时的验证码*/
    public static final String KEY_SMS_CODE_Login="SMS:LOGIN:";
    public static final String KEY_SMS_CODE_REALNAME="SMS:REALNAME:";
    /*redis自增ID作订单号*/
    public static final String KEY_RECHARGE_ORDERID="RECHARGE:ORDERID:SEQ";
    /*订单号redis*/
    public static final String KEY_ORDERID_SET="RECHARGE:ORDERID:SET";
}
