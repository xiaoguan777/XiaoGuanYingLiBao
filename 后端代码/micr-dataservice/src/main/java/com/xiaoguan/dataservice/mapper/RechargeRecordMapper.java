package com.xiaoguan.dataservice.mapper;

import com.xiaoguan.api.model.RechargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    //根据用户id查询充值信息
    List<RechargeRecord> selectByUid(@Param("uid") Integer uid,
                                     @Param("offset") Integer offset,
                                     @Param("pageSize") Integer pageSize);
    //根据用户id查询投资条数
    Integer selectCountByUid(@Param("uid") Integer uid);
    //根据订单号查订单记录
    RechargeRecord selectByRechargeNo(@Param("orderId") String orderId);
}