package com.high;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// @EnableEurekaClient // 将服务注册到Eureka注册中心，Eureka提供
@EnableDiscoveryClient // 将服务注册到注册中心(各种注册中心，如Zookeeper、Eureka、Nacos等)，SpringCloud提供
public class EurekaClientConsumerApplication {
    @Bean
    @LoadBalanced // Ribbon负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientConsumerApplication.class, args);
    }

}
