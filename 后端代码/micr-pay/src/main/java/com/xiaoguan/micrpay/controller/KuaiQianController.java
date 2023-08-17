package com.xiaoguan.micrpay.controller;

import com.xiaoguan.api.pojo.UserAccountInfo;
import com.xiaoguan.micrpay.service.KuiQianService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/kq")
public class KuaiQianController {
    @Resource
    private KuiQianService kuiQianService;
    /*接收支付充值请求*/
    @GetMapping("/rece/recharge")
    public String receiveKQRecharge(@RequestParam("uid") Integer uid, @RequestParam("money") BigDecimal money, Model model){
        String view="err";//默认错误视图
        try {
            if(uid!=null&&uid>0&&money!=null&&money.doubleValue()>0){

                //检查uid是否是有效的用户
                UserAccountInfo userAccountInfo = kuiQianService.queryUserId(uid);
                if(userAccountInfo!=null){
                    //创建快钱支付接口需要请求的参数
                    Map<String, String> data = kuiQianService.generateFormData(uid, money, userAccountInfo.getPhone());
                    model.addAllAttributes(data);
                    //创建充值记录
                    kuiQianService.addRecharge(uid,money,data.get("orderId"));
                    //订单号存放到redis
                    kuiQianService.addOrderIdToRedis(data.get("orderId"));
                    view="kqForm";
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
    //接收快钱给商家的支付地址
    @GetMapping("/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request){
        kuiQianService.kqNotify(request);
        return "<result>1</result><redirecturl>http://localhost:8080</redirecturl>";
    }
    //从定时任务调用的接口，查询快钱的订单是否成功
    @GetMapping("/check/recharge")
    @ResponseBody
    public String queryKQOder(){
        kuiQianService.handleCheckOrder();
        return "接收了查询的请求";
    }
}
