package com.onlylove.www.rabbitmq.amqp;

import com.onlylove.www.rabbitmq.MQConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static final String QUEUE_NAME = "test_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("消费者启动");
        //1建立mq连接
        Connection connection = MQConnectionUtils.newConnection();

        //2创建通道
       final   Channel channel = connection.createChannel();

        //3消费申明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //5消费监听消息
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("消费者获取生产消息" + msg);

            }
        };
        //第二个参数true，自动应答模式 false手动应答 需要些callback方法
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

    }

}
