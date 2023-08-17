package com.xiaoguan.front.controller;

import com.xiaoguan.api.model.RechargeRecord;
import com.xiaoguan.common.enums.Rcode;
import com.xiaoguan.front.view.PageInfo;
import com.xiaoguan.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "充值表相关功能")
@RestController
@RequestMapping("/v1/recharge")
public class RechargeController extends BaseController{
    @ApiOperation(value = "查询充值流水",notes = "流水按最近时间查询")
    @GetMapping("/records")
    public RespResult queryRechargeList(@RequestHeader("uid") Integer uid,
                                        @RequestParam(required = false,defaultValue ="1",value = "pageNo") Integer pageNo,
                                        @RequestParam(required = false,defaultValue ="6",value ="pageSize" ) Integer pageSize){
        RespResult respResult=RespResult.fail();
        if(uid!=null&&uid>0){
            Integer recordNums=rechargeService.queryRechargeNumsByUid(uid);
            if(recordNums!=null&&recordNums>0){
                respResult=RespResult.ok();
                PageInfo pageInfo = new PageInfo(pageNo, pageSize, recordNums);
                respResult.setPageInfo(pageInfo);
                List<RechargeRecord> rechargeRecords = rechargeService.queryRechargeByUid(uid, pageNo, pageSize);
                respResult.setList(rechargeRecords);
            }else {
                respResult.setRcode(Rcode.RESULT_BLANK);
            }

        }else {
            respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
        }
        return respResult;
    }
}
