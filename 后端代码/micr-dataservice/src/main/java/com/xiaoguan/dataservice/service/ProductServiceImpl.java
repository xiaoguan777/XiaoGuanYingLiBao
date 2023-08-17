package com.xiaoguan.dataservice.service;

import com.xiaoguan.api.model.ProductInfo;
import com.xiaoguan.api.pojo.MultiProduct;
import com.xiaoguan.api.service.ProductService;
import com.xiaoguan.common.constant.YLBConstant;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@DubboService(interfaceClass = ProductService.class,version = "1.0")
public class ProductServiceImpl implements ProductService{
    @Resource
    private ProductInfoMapper productInfoMapper;
    //根据产品id查询产品详情
    @Override
    public ProductInfo queryProductInfoById(Integer id) {
        ProductInfo productInfo=null;
        if (id!=null&&id>0) {
            productInfo=productInfoMapper.selectByPrimaryKey(id);
        }
        return productInfo;
    }

    //查询平台基本信息
    @Override
    public List<ProductInfo> queryByTypeLimit(Integer ptype, Integer pageNo, Integer pageSize) {
        List<ProductInfo> productInfos=new ArrayList<>();
        if (ptype==0||ptype==1||ptype==2) {
            pageNo= CommonUtil.defaultPageNo(pageNo);
            pageSize=CommonUtil.defaultPageSize(pageSize);
            int offset=(pageNo-1)*pageSize;
            productInfos = productInfoMapper.selectByTypeLimit(ptype, offset, pageSize);
        }

        return productInfos;
    }

    @Override
    public MultiProduct queyIndexPageProducts() {
        MultiProduct multiProduct=new MultiProduct();
        List<ProductInfo> xinShouBaoList=productInfoMapper.selectByTypeLimit(YLBConstant.PRODUCT_TYPE_XINSHOUBAO_0,
                                                            0,1);
        List<ProductInfo> youXuanList=productInfoMapper.selectByTypeLimit(YLBConstant.PRODUCT_TYPE_YOUXUAN_1,
                                                            0,3);
        List<ProductInfo> shanBiaoList=productInfoMapper.selectByTypeLimit(YLBConstant.PRODUCT_TYPE_SHANBIAO_2,
                                                            0,3);
        multiProduct.setXinShouBao(xinShouBaoList);
        multiProduct.setYouXuan(youXuanList);
        multiProduct.setSanBiao(shanBiaoList);
        return multiProduct;
    }
//查询产品数量
    @Override
    public Integer queryRecordNumsByType(Integer ptype) {
        Integer counts=0;
        if (ptype==0||ptype==1||ptype==2){
            counts=productInfoMapper.selectCountByType(ptype);
        }
        return counts;
    }
}