package com.xiaoguan.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xiaoguan.common.constant.RedisKey;
import com.xiaoguan.common.enums.SendCodeWays;
import com.xiaoguan.common.util.HttpUtils;
import com.xiaoguan.front.aliyun.AliyunSmsConfig;
import com.xiaoguan.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsCodeImpl implements SmsService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AliyunSmsConfig smsConfig;

    @Override
    public boolean checkSmsCode(String phone, String code,SendCodeWays sendCodeWays) {
        String way=RedisKey.KEY_SMS_CODE_Login;
        if(sendCodeWays==SendCodeWays.Register){
            way=RedisKey.KEY_SMS_CODE_REG;
        }else if(sendCodeWays==SendCodeWays.Login){
            way=RedisKey.KEY_SMS_CODE_Login;
        }else if(sendCodeWays==SendCodeWays.RealName){
            way=RedisKey.KEY_SMS_CODE_REALNAME;
        }
        String key=way+phone;
        if (stringRedisTemplate.hasKey(key)) {
            String queryCode = stringRedisTemplate.boundValueOps(key).get();
            if (code!=null&&code.equals(queryCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean sendSms(String phone, SendCodeWays sendCodeWays) {
            String sendCodeAim=RedisKey.KEY_SMS_CODE_Login;
            //选择发送短信的目的
            if(sendCodeWays==SendCodeWays.Register){
                sendCodeAim=RedisKey.KEY_SMS_CODE_REG;
            }else if(sendCodeWays==SendCodeWays.Login){
                sendCodeAim=RedisKey.KEY_SMS_CODE_Login;
            }else if(sendCodeWays==SendCodeWays.RealName){
                sendCodeAim=RedisKey.KEY_SMS_CODE_REALNAME;
            }

            boolean flag=false;
            String random= RandomStringUtils.randomNumeric(4);
            String host = smsConfig.getHost();
            String path = smsConfig.getPath();
            String method = smsConfig.getMethod();
            String appcode = smsConfig.getAppcode();
            Map<String, String> headers = new HashMap<>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            //根据API的要求，定义相对应的Content-Type
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String, String> querys = new HashMap<>();
            Map<String, String> bodys = new HashMap<>();
            bodys.put("content", "code:"+random+",expire_at:"+smsConfig.getSsmTime());
            bodys.put("template_id", smsConfig.getTemplateId());
            bodys.put("phone_number", phone);
            try {
                HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
                if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
                    String text = EntityUtils.toString(response.getEntity());
                    if (StringUtils.isNotBlank(text)) {
                        JSONObject jsonObject = JSONObject.parseObject(text);
                        if ("OK".equals(jsonObject.getString("status"))) {
                            flag=true;
                            String key= sendCodeAim+phone;
                            //短信验证码存储到redis
                            stringRedisTemplate.boundValueOps(key).set(random,3, TimeUnit.MINUTES);
                        }
                    }
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        return flag;
    }

    @Override
    public boolean sendSms(String phone) {
        boolean flag=false;
        String random= RandomStringUtils.randomNumeric(4);
        String host = smsConfig.getHost();
        String path = smsConfig.getPath();
        String method = smsConfig.getMethod();
        String appcode = smsConfig.getAppcode();
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("content", "code:"+random+",expire_at:"+smsConfig.getSsmTime());
        bodys.put("template_id", smsConfig.getTemplateId());
        bodys.put("phone_number", phone);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
                String text = EntityUtils.toString(response.getEntity());
                if (StringUtils.isNotBlank(text)) {
                    JSONObject jsonObject = JSONObject.parseObject(text);
                    if ("OK".equals(jsonObject.getString("status"))) {
                        flag=true;
                        String key= RedisKey.KEY_SMS_CODE_Login+phone;
                        //短信验证码存储到redis
                        stringRedisTemplate.boundValueOps(key).set(random,3, TimeUnit.MINUTES);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
