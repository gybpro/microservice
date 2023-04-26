package com.high.springcloud.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class RabbitMQConfig {
    private final Exchange directExchange;

    private final Queue directQueue;

    @Lazy
    public RabbitMQConfig(Exchange directExchange, Queue directQueue) {
        this.directExchange = directExchange;
        this.directQueue = directQueue;
    }

    // 声明Direct交换机
    @Bean
    public Exchange directExchange() {
        // 默认创建的是持久化、非自动删除的交换机
        return new DirectExchange("directExchange");
    }

    // 声明Direct消息队列
    @Bean
    public Queue directQueue() {
        // 排外：只允许一个消费者绑定/监听，其他消费者绑定/监听会报异常
        // 默认创建的是持久化、非排外(可多绑定/监听，先到先得)、非自动删除的消息队列
        return new Queue("directQueue");
    }

    // 绑定交换机和消息队列的关系
    @Bean
    public Binding directBinding() {
        return BindingBuilder
                // 绑定消息队列
                .bind(directQueue)
                // 绑定到要监听的交换机
                .to(directExchange)
                // 绑定路由键
                .with("directRoutingKey")
                // 没有其他参数
                .noargs();
    }

    // 声明Topic交换机
    @Bean
    public Exchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    // 声明Topic消息队列
    @Bean
    public Queue topicQueue1() {
        // 排外：只允许一个消费者绑定/监听，其他消费者绑定/监听会报异常
        // 默认创建的是持久化、非排外(可多绑定/监听，先到先得)、非自动删除的消息队列
        return new Queue("topicQueue1");
    }

    @Bean
    public Queue topicQueue2() {
        // 排外：只允许一个消费者绑定/监听，其他消费者绑定/监听会报异常
        // 默认创建的是持久化、非排外(可多绑定/监听，先到先得)、非自动删除的消息队列
        return new Queue("topicQueue2");
    }

    @Bean
    public Queue topicQueue3() {
        // 排外：只允许一个消费者绑定/监听，其他消费者绑定/监听会报异常
        // 默认创建的是持久化、非排外(可多绑定/监听，先到先得)、非自动删除的消息队列
        return new Queue("topicQueue3");
    }

    // 绑定交换机和消息队列的关系
    /*
    路由键指定的匹配规则：
        aa, aa.bb, aa.bb.cc

        aa      ->  aa
        aa.*    ->  aa.bb
        aa.#    ->  aa, aa.bb, aa.bb.cc
     */
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("aa").noargs();
    }

    @Bean
    public Binding topicBinding2() {
        // 通配符*：代表必须匹配一个单词
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("aa.*").noargs();
    }

    @Bean
    public Binding topicBinding3() {
        // 通配符#：代表匹配0或多个单词
        return BindingBuilder.bind(topicQueue3()).to(topicExchange()).with("aa.#").noargs();
    }
}
