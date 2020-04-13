package com.xc.manager_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EntityScan("com.xc.model.cms")  // 扫描实体类
@ComponentScans({
        @ComponentScan("com.xc.api"), // 扫描接口
        @ComponentScan("com.xc.ommon") // 扫描接口
})
@EnableEurekaClient
public class ManagerCmsMain {

    public static void main(String[] args) {

        SpringApplication.run(ManagerCmsMain.class, args);

    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }



}
