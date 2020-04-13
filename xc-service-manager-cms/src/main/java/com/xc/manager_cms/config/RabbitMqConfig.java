package com.xc.manager_cms.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.directExchange("ex_routing_cms_postpage").durable(true).build();
    }

}
