package com.high.springcloud.msg;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ReceiveMsgListener {
    @RabbitListener(queues = {"directQueue"})
    public void receiveDirectMsg(Message msg) {
        // 默认是自动应答消息，消息被接收时，自动从消息队列中消费掉(移除)
        printMsg(msg);
    }

    // fanout交换机创建的是非持久化、排外、自动删除的消息队列
    @RabbitListener(bindings = {@QueueBinding(
            // 如果没有指定消息队列名称，则使用随机名称
            value = @Queue,
            exchange = @Exchange(
                    // 声明交换机类型
                    type = "fanout",
                    // 声明交换机名称
                    name = "fanoutExchange"
            )
    )})
    public void receiveFanoutMsg(Message msg, Channel channel) {
        // 业务逻辑完成后，消费消息
        printMsg(msg);

        // 手动应答配置
        try {
            channel.basicAck(
                    // 消息唯一标识
                    msg.getMessageProperties().getDeliveryTag(),
                    // 是否进行批量操作
                    false
            );
        } catch (IOException e) {
            e.printStackTrace();
            try {
                channel.basicReject(
                        // 消息唯一标识
                        msg.getMessageProperties().getDeliveryTag(),
                        // 是否进行批量操作
                        false
                );
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }

    // @RabbitListener(queues = {"topicQueue1"})
    @RabbitListener(queues = {"topicQueue2"})
    // @RabbitListener(queues = {"topicQueue3"})
    public void receiveTopicMsg(Message msg) {
        // 默认是自动应答消息，消息被接收时，自动从消息队列中消费掉(移除)
        printMsg(msg);
    }

    private void printMsg(Message msg) {
        // 默认是自动应答消息，消息被接收时，自动从消息队列中消费掉(移除)
        System.out.println("接收到的消息：");
        System.out.println("\t消息唯一标识：" + msg.getMessageProperties().getDeliveryTag());
        System.out.println("\t消息队列：" + msg.getMessageProperties().getConsumerQueue());
        System.out.println("\t队列唯一标识：" + msg.getMessageProperties().getConsumerTag());
        System.out.println("\t消息内容：" + new String(msg.getBody()));
    }
}
