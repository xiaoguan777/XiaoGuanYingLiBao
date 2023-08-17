package com.xiaoguan.front.controller;

import com.xiaoguan.common.constant.RedisKey;
import com.xiaoguan.common.enums.Rcode;
import com.xiaoguan.common.enums.SendCodeWays;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "短信验证码相关功能")
@RestController
@RequestMapping("/v1/sms")
public class SmsController extends BaseController{
    @ApiOperation(value = "注册验证码发送",notes = "发送注册的验证码")
    @GetMapping("/code/register")
    public RespResult sendCodeRegister(@RequestParam("phone") String phone){
        RespResult respResult=RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            String key= RedisKey.KEY_SMS_CODE_REG+phone;
            if (stringRedisTemplate.hasKey(key)) {
                respResult=RespResult.ok();
                respResult.setRcode(Rcode.SMSCODE_CAN_USE);
            }else {
                boolean isSuccess = smsService.sendSms(phone, SendCodeWays.Register);
                if (isSuccess) {
                    respResult=RespResult.ok();
                }
            }
        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERROR);
        }
        return respResult;
    }

    @ApiOperation(value = "登录验证码发送",notes = "发送登录的验证码")
    @GetMapping("/code/login")
    public RespResult sendCodeLogin(@RequestParam("phone") String phone){
        RespResult respResult=RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            String key= RedisKey.KEY_SMS_CODE_Login+phone;
            if (stringRedisTemplate.hasKey(key)) {
                respResult=RespResult.ok();
                respResult.setRcode(Rcode.SMSCODE_CAN_USE);
            }else {
                boolean isSuccess = smsService.sendSms(phone);
                if (isSuccess) {
                    respResult=RespResult.ok();
                }
            }
        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERROR);
        }
        return respResult;
    }
    @ApiOperation(value = "实名验证验证码发送",notes = "发送实名认证的验证码")
    @GetMapping("/code/realname")
    public RespResult sendCodeRealName(@RequestParam("phone") String phone){
        RespResult respResult=RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            String key= RedisKey.KEY_SMS_CODE_REALNAME+phone;
            if (stringRedisTemplate.hasKey(key)) {
                respResult=RespResult.ok();
                respResult.setRcode(Rcode.SMSCODE_CAN_USE);
            }else {
                boolean isSuccess = smsService.sendSms(phone,SendCodeWays.RealName);
                if (isSuccess) {
                    respResult=RespResult.ok();
                }
            }
        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERROR);
        }
        return respResult;
    }
}
