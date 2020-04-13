package com.xc.filesystem.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
public class DfsConfig {

    @Value("${xc.fastdfs.connect_timeout_in_seconds}")
    public int connectTimeoutInSeconds;
    @Value("${xc.fastdfs.network_timeout_in_seconds}")
    public int networkTimeoutInSeconds;
    @Value("${xc.fastdfs.charset}")
    public String charset;
    @Value("${xc.fastdfs.tracker_servers}")
    public String trackerServers;

}
