package com.xiaoguan.common.enums;

public enum Rcode {
    UNKOWN(8,"请稍后重试"),
    SUCCESS(7,"请求成功"),
    REQUEST_PARAM_ERROR(1001,"请求参数有误"),
    REQUEST_PRODUCT_ERROR(1002,"产品类型有误"),
    REQUEST_PRODUCT_NOT_FOUND(1003,"产品已经下线"),
    PHONE_FORMAT_ERROR(1004,"手机号格式不正确"),
    PHONE_EXISTS(1005,"手机号已经注册"),
    SMSCODE_CAN_USE(1006,"验证码有效期三分钟"),
    SMSCODE_INVALID(1007,"验证码不匹配，无效"),
    USER_NOT_EXISTS(1008,"用户不存在"),
    TOKEN_ERROR(1009,"签名异常"),
    PASSWORD_ERROR(1010,"密码错误"),
    UDDATE_DATABASE_ERROR(1011,"更新数据库错误"),
    JUDGE_NAME_IDCARD_FAILD(1012,"姓名和身份证号匹配失败"),
    PHONE_INVALID(1013,"用户已经实名认证"),
    TOKEN_INVALID(3000,"签名无效"),
    RESULT_BLANK(9,"查询结果为空"),
    UPDATE_EXCEPTION(2000,"更新失败"),
    MONEY_NOT_ENOGHF(2001,"账户余额不足"),
    PRODUCT_ERROR(2002,"产品不存在"),
    EXCEED_LEFTMONET(2003,"超过产品可投资金额"),
    INVEST_SCOPE_ERROR(2004,"不符合产品投资区间"),
    NEED_REAL_NAME(2005,"需要实名认证");

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * 7请求成功
     * 8请求失败
     */
    private int code;
    private String text;
    Rcode(int c,String t){
        this.code=c;
        this.text=t;
    }
}
