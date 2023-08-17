package com.xiaoguan.micrpay.settings;

import com.xiaoguan.micrpay.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PayWebMvcConfiguration implements WebMvcConfigurer {
    @Value("${jwt.secretKey}")
    private String jwtSecret;
    @Value("${interceptor.url}")
    private List<String> addPath;

    public String getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(String tokenTime) {
        this.tokenTime = tokenTime;
    }

    @Value("jwt.time")
    private String tokenTime;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtSecret);
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(addPath);
    }

    //处理跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //处理请求的地址
        registry.addMapping("/**")
                //允许跨域的网站
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");

    }
}
