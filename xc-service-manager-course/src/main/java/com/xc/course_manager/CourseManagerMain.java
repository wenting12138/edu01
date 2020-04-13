package com.xc.course_manager;

import com.xc.ommon.interceptor.FeignClientInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@MapperScan("com.xc.course_manager.dao")
@ComponentScans({
        @ComponentScan("com.xc.ommon"),
        @ComponentScan("com.xc.api.config"),
})
@EnableFeignClients
@EnableDiscoveryClient
public class CourseManagerMain {

    public static void main(String[] args) {

        SpringApplication.run(CourseManagerMain.class, args);

    }

    /**
     *  定义feign拦截器, 实现微服务间的jwt的传递
     */
    @Bean
    public FeignClientInterceptor feignClientInterceptor(){
        return new FeignClientInterceptor();
    }

}
