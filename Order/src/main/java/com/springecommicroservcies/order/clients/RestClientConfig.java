package com.springecommicroservcies.order.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @LoadBalanced
    @Bean
    public RestClient.Builder restClientBuilderLb(){
        return RestClient.builder();
    }

    //Modification needed to avoid circular dependency
    @Primary
    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
