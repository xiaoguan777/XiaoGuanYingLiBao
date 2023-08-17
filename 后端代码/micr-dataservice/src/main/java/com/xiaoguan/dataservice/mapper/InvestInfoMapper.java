package com.xiaoguan.dataservice.mapper;

import com.xiaoguan.api.model.InvestInfo;
import com.xiaoguan.api.pojo.BidInfoProduct;
import com.xiaoguan.api.pojo.InvestInfoPlus;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface InvestInfoMapper {
    /*累计成交金额*/
    BigDecimal selectSumInvestMoney();
    int deleteByPrimaryKey(Integer id);

    int insert(InvestInfo record);

    int insertSelective(InvestInfo record);

    InvestInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvestInfo record);

    int updateByPrimaryKey(InvestInfo record);
    /*某个产品投资记录*/

    List<BidInfoProduct> selectBidInfoByProductId(@Param("proId") Integer proId,
                                                  @Param("offset") Integer offset,
                                                  @Param("rows") Integer rows);
    /*根据用户id查询投资记录及产品名称*/
    List<InvestInfoPlus> selectByUid(@Param("uid") Integer uid,@Param("offset") int offset,@Param("pageSize") Integer pageSize);
    /*根据用户id查询投资记录数量*/
    Integer selectCountByUid(Integer uid);
    /*根据产品id查所有投资信息*/
    List<InvestInfo> selectBidInfoByProId(@Param("proId") Integer proId);
}