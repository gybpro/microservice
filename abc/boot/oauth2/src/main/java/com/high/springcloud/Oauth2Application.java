package com.high.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Oauth2集成了Security：
 *      1.SpringSecurity的配置类，用于认证操作
 *      2.Oauth2提供授权服务器，用于颁发token
 *      3.Oauth2提供资源服务器，用于校验token
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
// 1.开启Security认证
@EnableWebSecurity
// 2.开启Oauth2的授权服务器
@EnableAuthorizationServer
// 3.开启Oauth2的资源服务器
@EnableResourceServer
// 开启发现客户端
@EnableDiscoveryClient
@SpringBootApplication
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
