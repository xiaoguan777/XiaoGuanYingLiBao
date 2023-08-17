package com.xiaoguan.test;



import com.alibaba.fastjson.JSONObject;
import com.xiaoguan.common.util.HttpUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RealNameTest {
    @Test
    public void testRealName(){
        String host = "https://eid.shumaidata.com";
        String path = "/eid/check";
        String method = "POST";
        String appcode = "77fdac608932484bb873fca5dc650c57";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("idcard", "522425199806064534");
        bodys.put("name", "关富军");
        //{"code":"0",
        // "message":"成功",
        // "result":{"name":"张三",
        // "idcard":"522425199806064534",
        // "res":"2",
        // "description":"不一致",(res为1为一致)
        // "sex":"男",
        // "birthday":"19980606",
        // "address":"贵州省毕节地区织金县"}}
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
                String text = EntityUtils.toString(response.getEntity());
                System.out.println(text);
                if (StringUtils.isNotBlank(text)) {
                    JSONObject jsonObject = JSONObject.parseObject(text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
