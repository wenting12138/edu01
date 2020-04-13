package com.xc.learning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@MapperScan("com.xc.course_manager.dao")
@ComponentScans({
        @ComponentScan("com.xc.ommon"),
        @ComponentScan("com.xc.api.config"),
})
@EntityScan("com.xc.model.learning")
@EnableFeignClients
@EnableDiscoveryClient
public class LearningMain {

    public static void main(String[] args) {

        SpringApplication.run(LearningMain.class, args);

    }

}
