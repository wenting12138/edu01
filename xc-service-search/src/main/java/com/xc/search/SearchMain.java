package com.xc.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScans({
        @ComponentScan("com.xc.ommon"),
        @ComponentScan("com.xc.api.config"),
})
@EnableFeignClients
@EnableDiscoveryClient
public class SearchMain {

    public static void main(String[] args) {
        SpringApplication.run(SearchMain.class);
    }

}
