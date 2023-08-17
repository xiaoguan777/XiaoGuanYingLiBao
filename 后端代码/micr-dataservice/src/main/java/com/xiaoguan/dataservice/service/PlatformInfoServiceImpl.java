package com.xiaoguan.dataservice.service;

import com.xiaoguan.api.pojo.BaseInfo;
import com.xiaoguan.api.service.PlatformBaseInfo;
import com.xiaoguan.dataservice.mapper.InvestInfoMapper;
import com.xiaoguan.dataservice.mapper.ProductInfoMapper;
import com.xiaoguan.dataservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;

@DubboService(interfaceClass = PlatformBaseInfo.class,version = "1.0")
public class PlatformInfoServiceImpl implements PlatformBaseInfo {
    //注入mapper
    @Resource
    private UserMapper userMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private InvestInfoMapper investInfoMapper;
    /*平台基本信息*/
    @Override
    public BaseInfo quertPlatBaseInfo() {
        //获取注册人数，收益率平均值，累计成交额
        int usersCount = userMapper.selectUsersCount();
        BigDecimal avgRate = productInfoMapper.selectAvgRate();
        BigDecimal sumInvestMoney = investInfoMapper.selectSumInvestMoney();
        BaseInfo baseInfo = new BaseInfo(avgRate, sumInvestMoney, usersCount);

        return baseInfo;
    }
}
