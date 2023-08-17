package com.xiaoguan.front.controller;

import com.xiaoguan.api.model.RechargeRecord;
import com.xiaoguan.api.pojo.InvestInfoPlus;
import com.xiaoguan.api.pojo.UserAccountInfo;
import com.xiaoguan.common.constant.RedisKey;
import com.xiaoguan.common.enums.Rcode;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.front.view.PageInfo;
import com.xiaoguan.front.view.RespResult;
import com.xiaoguan.front.view.invest.RankView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
@Api(tags = "投资理财产品")
//有关投资的功能
@RequestMapping("/v1")
@RestController
public class InvestController extends BaseController{
    @ApiOperation(value = "投资排行榜",notes = "显示投资排行榜前三名")
    /*投资排行榜*/
    @GetMapping("/invest/rank")
    public RespResult showInvestRank(){
        Set<ZSetOperations.TypedTuple<String>> sets =
                stringRedisTemplate.boundZSetOps(RedisKey.KEY_INVEST_RANK).reverseRangeWithScores(0, 2);
        List<RankView> rankViews=new ArrayList<>();
        sets.forEach(set->{
            rankViews.add(new RankView(CommonUtil.tuoMinPhone(set.getValue()),set.getScore()));
        });
        RespResult respResult=RespResult.ok();
        respResult.setList(rankViews);
        return respResult;
    }
    @ApiOperation(value = "投资信息表",notes = "返回投资信息和投资产品名称")
    @GetMapping("/invest/detail")
    public RespResult showInvestInfo(@RequestHeader("uid") Integer uid,
                                     @RequestParam(required = false,defaultValue ="1",value = "pageNo") Integer pageNo,
                                     @RequestParam(required = false,defaultValue ="6",value ="pageSize" ) Integer pageSize){
        RespResult respResult=RespResult.fail();
        if(uid!=null&&uid>0){
            Integer recordNums=investService.queryInvestNumsByUid(uid);
            if(recordNums!=null&&recordNums>0){
                respResult=RespResult.ok();
                PageInfo pageInfo = new PageInfo(pageNo, pageSize, recordNums);
                respResult.setPageInfo(pageInfo);
                List<InvestInfoPlus> investInfoPluses = investService.queryInvestInfoByUid(uid, pageNo, pageSize);
                respResult.setList(investInfoPluses);
            }else {
                respResult.setRcode(Rcode.RESULT_BLANK);
            }

        }else {
            respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
        }
        return respResult;
    }
    @ApiOperation(value = "投资产品",notes = "产品的投资功能")
    @PostMapping("/invest/product")
    public RespResult investProduct(@RequestHeader("uid") Integer uid,
                                    @RequestParam("productId") Integer productId,
                                    @RequestParam("money") BigDecimal money){
        RespResult respResult=RespResult.fail();
        if(uid!=null&&uid>0&&productId!=null&&productId>0&&money!=null&&money.doubleValue()>0){
            //检查实名认证
            UserAccountInfo userAccountInfo = userService.queryUserAccountInfo(uid);
            if(userAccountInfo!=null&& StringUtils.isNotBlank(userAccountInfo.getName())){
                try {
                    Integer result=investService.investProduct(uid,productId,money);
                    switch (result){
                        case (0):respResult.setRcode(Rcode.UNKOWN);
                            break;
                        case (1):respResult=RespResult.ok();
                            investService.modifyInvestRank(uid,money);//更新投资排行榜
                            break;
                        case (2):respResult.setRcode(Rcode.MONEY_NOT_ENOGHF);
                            break;
                        case (3):respResult.setRcode(Rcode.PRODUCT_ERROR);
                            break;
                        case (4):respResult.setRcode(Rcode.EXCEED_LEFTMONET);
                            break;
                        case (5):respResult.setRcode(Rcode.INVEST_SCOPE_ERROR);
                            break;
                        default:
                            respResult=RespResult.fail();
                    }
                }
                catch (Exception e) {
                    System.out.println("[warn]"+e.getMessage());
                    respResult.setRcode(Rcode.UPDATE_EXCEPTION);
                }
            }else {
                respResult.setRcode(Rcode.NEED_REAL_NAME);
            }

        }else {
            respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
        }
        return respResult;

    }
}
