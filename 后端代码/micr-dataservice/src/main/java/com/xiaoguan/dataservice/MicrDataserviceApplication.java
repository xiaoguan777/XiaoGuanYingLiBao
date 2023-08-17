package com.xiaoguan.dataservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//表示启动dubbo
@EnableDubbo
//Mapper扫描
@MapperScan(basePackages = "com.xiaoguan.dataservice.mapper")
@SpringBootApplication
public class MicrDataserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicrDataserviceApplication.class);
    }
}
