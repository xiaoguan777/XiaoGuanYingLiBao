package com.xiaoguan.front.controller;

import com.xiaoguan.api.model.ProductInfo;
import com.xiaoguan.api.pojo.BidInfoProduct;
import com.xiaoguan.api.pojo.MultiProduct;
import com.xiaoguan.common.enums.Rcode;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.front.view.PageInfo;
import com.xiaoguan.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "理财产品功能")
@RestController
@RequestMapping("/v1")
public class ProductController extends BaseController{
    @ApiOperation(value = "首页三类产品列表",notes = "一个新手表，三个优选，三个散标产品信息")
    @GetMapping("/product/index")
    public RespResult queryProductIndex(){
        RespResult respResult =RespResult.ok();
        MultiProduct multiProduct = productService.queyIndexPageProducts();
        respResult.setDate(multiProduct);
        return respResult;
    }
    /*按产品类型分页查询*/
    @ApiOperation(value = "某种类型产品列表",notes = "根据发送的产品类型，产品页码，产品数查询产品信息")
    @GetMapping("/product/list")
    public RespResult queryProductByType(@RequestParam(value = "pType",required = false) Integer pType,
                                         @RequestParam(value = "pageNo",required = false,defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "9") Integer pageSize){
        RespResult respResult = RespResult.fail();
        if(pType!=null&&(pType==0||pType==1||pType==2))
        {
            pageNo= CommonUtil.defaultPageNo(pageNo);
            pageSize=CommonUtil.defaultPageSize(pageSize);
            //产品记录总数
            Integer recordNums = productService.queryRecordNumsByType(pType);
            if (recordNums>0) {
                //产品列表
                List<ProductInfo> productInfos = productService.queryByTypeLimit(pType, pageNo, pageSize);
                respResult=RespResult.ok();
                respResult.setList(productInfos);
                //构建pageInfo分页
                PageInfo pageInfo = new PageInfo(pageNo,pageSize,recordNums);
                respResult.setPageInfo(pageInfo);
            }
        }else {
            respResult.setRcode(Rcode.REQUEST_PRODUCT_ERROR);
        }
        return respResult;
    }
    //某种类型产品的投资用户信息
    @GetMapping("/productInvest/info")
    @ApiOperation(value = "某种类型产品的投资用户信息",notes = "返回投资记录id,脱敏手机号，投资金额，和最新投资时间")
    public RespResult queryProductInvestInfo(@RequestParam("productId") Integer id){
        RespResult respResult=RespResult.fail();
        if (id!=null&&id>0) {
            //调用产品查询
            ProductInfo productInfo = productService.queryProductInfoById(id);
            if (productInfo != null) {
                List<BidInfoProduct> bidInfoProducts = investService.queryBidListByProductId(id, 1, 5);
                respResult=RespResult.ok();
                respResult.setDate(productInfo);
                respResult.setList(bidInfoProducts);
            }else {
                respResult.setRcode(Rcode.REQUEST_PRODUCT_NOT_FOUND);
            }
        }
        return respResult;
    }
}
