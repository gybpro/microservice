package com.high.springcloud.msg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Component
public class SendMsgProvider {
    /*
    RabbitTemplate：RabbitMQ的模板对象，它的父类是AmqpTemplate，也是消息发送模板对象
    转换并发送消息：
        void convertAndSend(String routingKey, Object message);
            参数1：路由键，该方法是将消息发送到默认交换机，通过它将消息进行路由，
            所以这里的routingKey实际上并不是路由键，而是消息队列的名称
            参数2：发送的消息
        void convertAndSend(String exchange, String routingKey, Object message);
            参数1：交换机名称
            参数2：路由键
            参数3：发送的消息
    在发送消息之前需要绑定交换机和消息队列的关系，这些关系可以通过配置文件完成
     */
    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public SendMsgProvider(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendDirectMsg() {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("id", "123");
        resultMap.put("username", "张三");
        resultMap.put("hobby", "狂飙");

        try {
            // 通过默认交换机进行消息路由到指定消息队列
            // rabbitTemplate.convertAndSend("directQueue", objectMapper.writeValueAsString(resultMap));
            // 通过指定交换机将消息路由到指定消息队列
            rabbitTemplate.convertAndSend("directExchange", "directRoutingKey", objectMapper.writeValueAsString(resultMap));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendFanoutMsg() {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("id", "2233");
        resultMap.put("username", "啦啦啦");
        resultMap.put("hobby", "学习");

        try {
            // 通过默认交换机进行消息路由到指定消息队列
            // rabbitTemplate.convertAndSend("fanoutQueue", objectMapper.writeValueAsString(resultMap));
            // 通过指定交换机将消息路由到指定消息队列
            rabbitTemplate.convertAndSend("fanoutExchange", "",
                    objectMapper.writeValueAsString(resultMap));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendTopicMsg() {
        Map<String, String> resultMap1 = new HashMap<>();
        resultMap1.put("id", "666");
        resultMap1.put("username", "李四");
        resultMap1.put("hobby", "篮球");
        resultMap1.put("routingKey", "aa");

        Map<String, String> resultMap2 = new HashMap<>();
        resultMap2.put("id", "666");
        resultMap2.put("username", "李四");
        resultMap2.put("hobby", "篮球");
        resultMap2.put("routingKey", "aa.bb");

        Map<String, String> resultMap3 = new HashMap<>();
        resultMap3.put("id", "666");
        resultMap3.put("username", "李四");
        resultMap3.put("hobby", "篮球");
        resultMap3.put("routingKey", "aa.bb.cc");

        try {
            // 通过默认交换机进行消息路由到指定消息队列
            // rabbitTemplate.convertAndSend("topicQueue", objectMapper.writeValueAsString(resultMap));
            // 通过指定交换机将消息路由到指定消息队列
            rabbitTemplate.convertAndSend("topicExchange", "aa",
                    objectMapper.writeValueAsString(resultMap1));
            rabbitTemplate.convertAndSend("topicExchange", "aa.bb",
                    objectMapper.writeValueAsString(resultMap2));
            rabbitTemplate.convertAndSend("topicExchange", "aa.bb.cc",
                    objectMapper.writeValueAsString(resultMap3));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
