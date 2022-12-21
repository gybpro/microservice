package com.high.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class NacosClientProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosClientProviderApplication.class, args);
    }
}
