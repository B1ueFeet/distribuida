package com.programacion.distribuida.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientConfig {

    @Bean
    @LoadBalanced
    RestTemplateBuilder loadBalancedRestTemplateBuilder() {
        return new RestTemplateBuilder();
    }

}


