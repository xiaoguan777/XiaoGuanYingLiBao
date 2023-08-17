package com.xiaoguan.api.service;

import com.xiaoguan.api.model.RechargeRecord;

import java.util.List;

public interface RechargeService {
    /*根据userId查询充值记录*/
    List<RechargeRecord> queryRechargeByUid(Integer uid,Integer pageNo,Integer pageSize);
    /*根据用户id查询投资条数*/
    Integer queryRechargeNumsByUid(Integer uid);
    /*添加充值记录*/
    int addRechargeRecode(RechargeRecord rechargeRecord);
    /*处理后续的充值*/
    int handleKQNotify(String orderId, String payAmount, String payResult);

}
