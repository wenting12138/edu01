package com.xc.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScans({
        @ComponentScan("com.xc.ommon"),
        @ComponentScan("com.xc.api.config")
})
public class FileSystemMain {

    public static void main(String[] args) {
        SpringApplication.run(FileSystemMain.class, args);
    }
}
