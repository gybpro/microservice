package com.high;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // 开启Eureka注册中心服务
public class EurekaServerCApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerCApplication.class, args);
    }

}
