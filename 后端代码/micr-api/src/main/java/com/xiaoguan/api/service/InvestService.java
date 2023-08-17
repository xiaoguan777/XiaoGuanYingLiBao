package com.xiaoguan.api.service;

import com.xiaoguan.api.pojo.BidInfoProduct;
import com.xiaoguan.api.pojo.InvestInfoPlus;

import java.math.BigDecimal;
import java.util.List;

public interface InvestService {
    /**
     * 查询某个产品的投资记录
     */
    List<BidInfoProduct> queryBidListByProductId(Integer proId,Integer pageNo,Integer pageSize);

    /**
     *根据用户id查询投资信息表
     */
    List<InvestInfoPlus> queryInvestInfoByUid(Integer uid, Integer pageNo, Integer pageSize);
    /**
     *根据用户id查询投资记录数量
     */
    Integer queryInvestNumsByUid(Integer uid);
    /**
     *投资产品功能
     * 返回值表示是否成功
     */
    Integer investProduct(Integer uid, Integer productId, BigDecimal money) throws Exception;
    /**
     *更新投资排行榜
     */
    void modifyInvestRank(Integer uid,BigDecimal money);
}
