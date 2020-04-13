package com.xc.ucenter;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@EnableFeignClients
@EnableDiscoveryClient
@EntityScan("com.xc.model.ucenter")
@ComponentScans({
        @ComponentScan("com.xc.model.ucenter"),
        @ComponentScan("com.xc.ommon"),
        @ComponentScan("com.xc.api.config"),
})
@MapperScan("com.xc.ucenter.dao")
@SpringBootApplication
public class UCenterMain {

    public static void main(String[] args) {

        SpringApplication.run(UCenterMain.class, args);

    }

}
