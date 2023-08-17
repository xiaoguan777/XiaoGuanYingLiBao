package com.xiaoguan.front.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigruationSettings {
    //创建Docket对象
    @Bean
    public Docket docket(){
        Docket docket=new Docket(DocumentationType.SWAGGER_2);
        //创建Api信息，接口文档的总体描述
        ApiInfo apiInfo=new ApiInfoBuilder().title("小关金融项目").version("1.0").description("这是一个前后端分离的项目")
                .contact(new Contact("小关","https://xiaoguan777.github.io/","2240474789@qq.com"))
                .license("Apache2.0").build();
        //设置使用ApiInfo
        docket=docket.apiInfo(apiInfo);
        //设置参与文档生成的包
        docket=docket.select().apis(RequestHandlerSelectors.basePackage("com.xiaoguan.front.controller")).build();
        return docket;
    }
}
