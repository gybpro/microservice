package com.high.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

// SpringBoot在启动时自动扫描启动类同包下的所有文件，扫描位置是可以自定义的
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
// @EnableEurekaClient // 将服务注册到Eureka注册中心，Eureka提供
@EnableDiscoveryClient // 将服务注册到注册中心(各种注册中心，如Zookeeper、Eureka、Nacos等)，SpringCloud提供
// Feign也是同SpringBoot一样，启动的时候扫描启动类同包下的所有文件，可以通过指定包提高效率
@EnableFeignClients(basePackages = "com.high.springcloud.feign")
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
