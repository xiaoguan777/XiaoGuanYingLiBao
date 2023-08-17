package com.xiaoguan.micrpay.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.xiaoguan.common.enums.Rcode;
import com.xiaoguan.common.util.JwtUtil;
import com.xiaoguan.front.view.RespResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TokenInterceptor implements HandlerInterceptor {
    private String secret="";

    public TokenInterceptor(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.如果是OPTIONS,放行
        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }
        //获取token的值并进行验证
        boolean userLogin=false;//用户是否是登录状态
        try {
            String headerUid = request.getHeader("uid");
            String headerToken=request.getHeader("Authorization" );
            if (StringUtils.isNotBlank(headerToken)) {
                String token = headerToken.substring(7);
                JwtUtil jwtUtil = new JwtUtil(secret);
                Claims jwtInfo = jwtUtil.readJwt(token);
                Integer jwtuid = jwtInfo.get("uid", Integer.class);
                if(headerUid!=null){
                    if(headerUid.equals(String.valueOf(jwtuid))){
                        //两者一样认为已经登录
                        userLogin=true;
                    }
                }

            }
        }catch (Exception e){
            System.out.println("[warn]"+e.getMessage());
        }finally {
            if (userLogin==false) {
                RespResult respResult=RespResult.fail();
                respResult.setRcode(Rcode.TOKEN_INVALID);
                respResult.setDate(null);
                respResult.setList(null);
                respResult.setAccessToken("");
                respResult.setPageInfo(null);
                String jsonString = JSONObject.toJSONString(respResult);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(jsonString);
                out.flush();
                out.close();
            }
        }

        return userLogin;
    }
}
