package com.xiaoguan.task;

import com.xiaoguan.api.service.IncomeService;
import com.xiaoguan.common.util.HttpUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class TaskManager {
    /**
     * 定义方法表示执行的定时任务功能
     * 方法定义的要求
     * 1.public公共方法
     * 2.方法没有参数
     * 3.方法没有返回值
     * 4.方法的上面加入@Scheduled，设置cron属性
     */
//    @Scheduled(cron = "1 23 17 * * ?")
//    public void testCron(){
//        System.out.println("执行了定时方法"+new Date());
//    }
    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    private IncomeService incomeService;
    /*生成收益计划*/
    @Scheduled(cron ="0 0/25 * * * ? " )
    public void invokeGeneratePlan(){
        try {
            incomeService.generateIncomePlan();
        } catch (Exception e) {
            System.out.println("[error]"+e.getMessage());
        }
    }
    /*生成收益返还*/
    @Scheduled(cron ="0 0/30 * * * ? " )
    public void invokeIncomeReturn(){
        try {
            incomeService.InvokeIncomeBack();
        } catch (Exception e) {
            System.out.println("[error]"+e.getMessage());
        }
    }
    /*调用补单接口*/
    @Scheduled(cron ="0 0/20 * * * ?  " )
    public void invokeKuaiQianQuery(){
        try {
            //发起get请求,编程方式处理http请求
            String url="http://localhost:9000/pay/kq/check/recharge";
            //1.创建HttpClient对象
            CloseableHttpClient client= HttpClients.createDefault();
            //2.创建http的get对象
            HttpGet httpGet=new HttpGet(url);
            //3.执行请求
            CloseableHttpResponse response = client.execute(httpGet);
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
