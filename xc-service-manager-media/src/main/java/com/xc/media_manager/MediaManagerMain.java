package com.xc.media_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScans({
        @ComponentScan("com.xc.ommon"),
        @ComponentScan("com.xc.api.config"),
})
public class MediaManagerMain {

    public static void main(String[] args) {

        SpringApplication.run(MediaManagerMain.class, args);

    }

}
