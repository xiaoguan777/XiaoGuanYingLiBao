package com.xiaoguan.dataservice.mapper;

import com.xiaoguan.api.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);
    //查询uid的账户信息并且上锁
    FinanceAccount selectBuUidForUpdate(@Param("uid") Integer uid);
    //充值更新订单信息
    int updateAvailableMoneyByRecharge(@Param("uid") Integer uid,@Param("rechargeMoney") BigDecimal rechargeMoney);
}