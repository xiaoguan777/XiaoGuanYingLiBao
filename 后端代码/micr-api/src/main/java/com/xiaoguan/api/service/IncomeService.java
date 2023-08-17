package com.xiaoguan.api.service;

import com.xiaoguan.api.pojo.IncomeInfoPlus;

import java.util.List;

public interface IncomeService {
    /*根据用户id查询收益信息和收益产品*/
    List<IncomeInfoPlus> queryIncomeInfoByUid(Integer uid, Integer pageNo, Integer pageSize);
    /*根据用户id查询收益记录数*/
    Integer queryIncomeNumsByUid(Integer uid);
    /*收益计划*/
    void generateIncomePlan() throws Exception;
    /*收益返还*/
    void InvokeIncomeBack() throws Exception;

}
