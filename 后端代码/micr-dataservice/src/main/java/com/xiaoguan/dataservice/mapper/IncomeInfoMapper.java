package com.xiaoguan.dataservice.mapper;

import com.xiaoguan.api.model.IncomeInfo;
import com.xiaoguan.api.model.InvestInfo;
import com.xiaoguan.api.pojo.IncomeInfoPlus;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IncomeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeInfo record);

    int insertSelective(IncomeInfo record);

    IncomeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeInfo record);

    int updateByPrimaryKey(IncomeInfo record);
    //根据用户id查询收益信息和收益产品名称
    List<IncomeInfoPlus> selectByUid(@Param("uid") Integer uid,@Param("offset") int offset,@Param("pageSize") Integer pageSize);
    //根据用户id查询收益记录条数
    Integer selectCountByUid(@Param("uid") Integer uid);
    //时间比cur时间早,返还收益状态为incomBegin的记录
    List<IncomeInfo> selectByDeadline(@Param("cueTime") Date curTime,@Param("incomeBegin") Integer incomeBegin);
}