package com.xc.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableDiscoveryClient
@EntityScan("com.xc.model.ucenter")
@ComponentScans({
        @ComponentScan(basePackages = "com.xc.ommon"),
        @ComponentScan(basePackages = "com.xc.api.config")
})
@SpringBootApplication
public class UcenterAuthMain {

    public static void main(String[] args) {
        SpringApplication.run(UcenterAuthMain.class, args);
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
