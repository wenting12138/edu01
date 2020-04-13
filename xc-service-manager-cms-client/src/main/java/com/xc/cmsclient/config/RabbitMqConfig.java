package com.xc.cmsclient.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${xc.mq.routingKey}")
    public String routingKey;
    @Value("${xc.mq.queue}")
    public String queue;
    public static final String EXCHANGE = "ex_routing_cms_postpage";

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.directExchange("ex_routing_cms_postpage").durable(true).build();
    }

    @Bean
    public Queue queue(){
        return new Queue(queue);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey).noargs();
    }


}
