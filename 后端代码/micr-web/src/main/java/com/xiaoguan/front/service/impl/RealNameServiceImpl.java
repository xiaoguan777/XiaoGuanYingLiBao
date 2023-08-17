package com.xiaoguan.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xiaoguan.api.service.UserService;
import com.xiaoguan.common.util.HttpUtils;
import com.xiaoguan.front.UpdateException;
import com.xiaoguan.front.aliyun.AliyunRealNameConfig;
import com.xiaoguan.front.service.RealNameService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class RealNameServiceImpl implements RealNameService {
    @Resource
    private AliyunRealNameConfig aliyunRealNameConfig;
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;
    //判断名字和身份证号码是否匹配，如果匹配则储存在数据库
    @Override
    public boolean judgeNameAndCard(String name, String idCard,String phone) throws UpdateException {
        boolean result=false;
        String host = aliyunRealNameConfig.getHost();
        String path = aliyunRealNameConfig.getPath();
        String method = aliyunRealNameConfig.getMethod();
        String appcode = aliyunRealNameConfig.getAppcode();
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("idcard",idCard.trim() );
        bodys.put("name", name.trim());
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
                String text = EntityUtils.toString(response.getEntity());
//                System.out.println(text);
                if (StringUtils.isNotBlank(text)) {
                    JSONObject jsonObject = JSONObject.parseObject(text);
                    JSONObject chcekResult = jsonObject.getObject("result", JSONObject.class);
                    if("一致".equals(chcekResult.getString("description"))){
                        result=true;
                        boolean modifyResult= userService.modifyRealName(phone,name,idCard);
                        if(!modifyResult){
                            throw new UpdateException("实名认证信息更新失败");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(idCard.trim().equals("777777888866669999"))//测试专用,身份证号为777777888866669999时不用验证直接通过
        {
            result=true;
        }
        return result;
    }
}
