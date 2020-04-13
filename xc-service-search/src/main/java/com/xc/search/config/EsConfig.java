package com.xc.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

    @Value("${xc.elasticsearch.hostlist}")
    private String hostlist;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        String[] split = hostlist.split(",");
        HttpHost[] httpHosts = new HttpHost[split.length];
        for (int i = 0; i < httpHosts.length; i++) {
            String item = split[i];
            httpHosts[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }

    @Bean
    public RestClient RestClient(){
        String[] split = hostlist.split(",");
        HttpHost[] httpHosts = new HttpHost[split.length];
        for (int i = 0; i < httpHosts.length; i++) {
            String item = split[i];
            httpHosts[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        return RestClient.builder(httpHosts).build();
    }

}
