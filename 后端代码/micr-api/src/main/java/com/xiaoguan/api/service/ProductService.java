package com.xiaoguan.api.service;

import com.xiaoguan.api.model.ProductInfo;
import com.xiaoguan.api.pojo.MultiProduct;

import java.util.List;

public interface ProductService {
    /**
     * 按照种类分页查询产品信息
     */
    List<ProductInfo> queryByTypeLimit(Integer ptype, Integer pageNo, Integer pageSize);
    /**
     * 查询首页的多个产品数据
     */
    MultiProduct queyIndexPageProducts();
    /**
     * 某个产品类型的记录总数
     */
    Integer queryRecordNumsByType(Integer ptype);
    /**
     * 根据产品id查询产品详情
     */
    ProductInfo queryProductInfoById(Integer id);
}
