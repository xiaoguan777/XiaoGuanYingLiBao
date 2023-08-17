package com.xiaoguan.front.controller;

import com.xiaoguan.api.service.*;
import com.xiaoguan.common.constant.YLBConstant;
import com.xiaoguan.common.util.JwtUtil;
import com.xiaoguan.front.service.RealNameService;
import com.xiaoguan.front.service.SmsService;
import com.xiaoguan.front.settings.WebMvcConfiguration;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
public class BaseController {
    //声明公共的方法，属性等
    @DubboReference(interfaceClass = PlatformBaseInfo.class,version = "1.0")
    protected PlatformBaseInfo platformBaseInfo;
    //产品服务
    @DubboReference(interfaceClass = ProductService.class,version = "1.0")
    protected ProductService productService;
    //redis
    @Resource
    protected StringRedisTemplate stringRedisTemplate;
    @DubboReference(interfaceClass = InvestService.class,version = "1.0")
    protected InvestService investService;
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    protected UserService userService;
    @Resource
    protected SmsService smsService;
    @Resource
    protected JwtUtil jwtUtil;
    @Resource
    protected RealNameService realNameService;

    Integer tokenTime= YLBConstant.TOKEN_TIME_MINUTE;
    //充值服务
    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    protected RechargeService rechargeService;
    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    protected IncomeService incomeService;
    @Value("${jwt.secretKey}")
    protected String jwtSecret;
}
