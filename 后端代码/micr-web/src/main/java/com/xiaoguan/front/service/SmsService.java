package com.xiaoguan.front.service;

import com.xiaoguan.common.enums.SendCodeWays;

public interface SmsService {
    //发送短信
    boolean sendSms(String phone, SendCodeWays sendCodeWays);
    boolean sendSms(String phone);
    boolean checkSmsCode(String phone,String code,SendCodeWays sendCodeWays);
}
