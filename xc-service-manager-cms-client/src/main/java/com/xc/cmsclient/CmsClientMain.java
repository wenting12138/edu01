package com.xc.cmsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScans({
        @ComponentScan("com.xc.ommon") // 扫描接口
})
@EnableDiscoveryClient
public class CmsClientMain {

    public static void main(String[] args) {

        SpringApplication.run(CmsClientMain.class, args);

    }

}
