package com.xiaoguan.front.controller;

import com.xiaoguan.api.pojo.IncomeInfoPlus;
import com.xiaoguan.api.pojo.InvestInfoPlus;
import com.xiaoguan.common.enums.Rcode;
import com.xiaoguan.front.view.PageInfo;
import com.xiaoguan.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "收益信息")
//有关投资的功能
@RequestMapping("/v1/income")
@RestController
public class IncomeController extends BaseController{
    @ApiOperation(value = "收益信息表",notes = "返回收益信息和投资产品名称")
    @GetMapping("/detail")
    public RespResult showInvestInfo(@RequestHeader("uid") Integer uid,
                                     @RequestParam(required = false,defaultValue ="1",value = "pageNo") Integer pageNo,
                                     @RequestParam(required = false,defaultValue ="6",value ="pageSize" ) Integer pageSize){
        RespResult respResult=RespResult.fail();
        if(uid!=null&&uid>0){
            Integer recordNums=incomeService.queryIncomeNumsByUid(uid);
            if(recordNums!=null&&recordNums>0){
                respResult=RespResult.ok();
                PageInfo pageInfo = new PageInfo(pageNo, pageSize, recordNums);
                respResult.setPageInfo(pageInfo);
                List<IncomeInfoPlus> incomeInfoPluses = incomeService.queryIncomeInfoByUid(uid, pageNo, pageSize);
                respResult.setList(incomeInfoPluses);
            }else {
                respResult.setRcode(Rcode.RESULT_BLANK);
            }

        }else {
            respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
        }
        return respResult;
    }
}
