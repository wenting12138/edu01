package com.xc.media_manager.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${xc-service-manager-media.mq.routingkey-media-video}")
    public String routingKey;
    public static final String EXCHANGE = "xc_media_processor";

    // 消费者并发数
    public static final int DEFAULT_CONCURRENT = 10;

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.directExchange(EXCHANGE).durable(true).build();
    }



}
