package com.xiaoguan.dataservice.mapper;

import com.xiaoguan.api.model.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductInfoMapper {
    /*按产品类型分页查询*/
    List<ProductInfo> selectByTypeLimit(@Param("ptype") Integer ptype,
                                        @Param("offset") Integer offset,
                                        @Param("rows") Integer rows);
    /*利率平均值*/
    BigDecimal selectAvgRate();
    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);
    //某个产品类型记录的总数
    Integer selectCountByType(@Param("ptype") Integer ptype);
    //根据主键查产品信息并且锁定
    ProductInfo selectByPrimaryKeyForUpdate(@Param("productId") Integer productId);
    //根据是否满标状态查产品
    List<ProductInfo> selectByStatus(@Param("status") Integer productStatusUnsold);
}