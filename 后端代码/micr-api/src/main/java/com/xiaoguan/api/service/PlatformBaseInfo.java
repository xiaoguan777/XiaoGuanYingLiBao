package com.xiaoguan.api.service;

import com.xiaoguan.api.model.ProductInfo;
import com.xiaoguan.api.pojo.BaseInfo;

import java.util.List;

public interface PlatformBaseInfo {
    /**
     * 计算利率，注册人数，累计成交金额
     */
    BaseInfo quertPlatBaseInfo();

}
