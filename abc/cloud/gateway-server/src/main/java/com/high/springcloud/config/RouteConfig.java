package com.high.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class RouteConfig {

    // 代码匹配路由，uri和path重复会自动匹配，不会报错，但yml配置会
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("guochuang-id", r -> r.path("/guochuang").uri("https://www.bilibili.com/guochuang"))
                .route("dance-id", r -> r.path("/v/dance").uri("https://www.bilibili.com"))
                .route("kichiku-id", r -> r.path("/v/kichiku").uri("https://www.bilibili.com"))
                .build();
    }
}
