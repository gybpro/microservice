package com.high.springcloud;

import com.high.springcloud.msg.SendMsgProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class NacosClientProviderApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(NacosClientProviderApplication.class, args);

        SendMsgProvider provider = run.getBean(SendMsgProvider.class);
        // provider.sendDirectMsg();
        // provider.sendFanoutMsg();
        provider.sendTopicMsg();

        System.out.println("消息发送完成。。。");
    }
}
