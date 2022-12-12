package com.high.springcloud.config;

import feign.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Configuration
// Feign也是同SpringBoot一样，启动的时候扫描启动类同包下的所有文件，可以通过指定包提高效率
@EnableFeignClients(basePackages = "com.high.springcloud.feign")
public class WebConfig {
    @Bean
    @LoadBalanced // Ribbon负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 打印Feign日志级别，Feign有4个日志级别
    // NONE不打印, BASIC基础(请求方法，URL，响应状态码，响应时间)
    // HEADERS标头(BASIC+请求头响应头), FULL所有信息(请求响应头体+元数据)
    // 元数据包括身份验证令牌、用于监视的请求标记，以及有关数据的信息，如数据的记录数(大小)等
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }
}
