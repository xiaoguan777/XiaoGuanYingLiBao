package com.xiaoguan.front.controller;

import com.xiaoguan.api.pojo.BaseInfo;
import com.xiaoguan.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = "平台信息功能")
@RestController
@RequestMapping("/v1")
public class PlatInfoController extends BaseController{
    /*平台基本信息*/
    @ApiOperation(value = "平台三项基本信息",notes = "注册人数，平均利率，总的投资金额")
    @GetMapping("/plat/info")
    public RespResult queryPlatBaseInfo(){
        //调用远程服务
        BaseInfo baseInfo = platformBaseInfo.quertPlatBaseInfo();
        RespResult respResult = new RespResult(7, "查询平台信息成功",baseInfo);//7表示正常
        return respResult;
    }
}
