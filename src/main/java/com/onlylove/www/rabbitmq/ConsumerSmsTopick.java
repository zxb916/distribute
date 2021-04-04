package com.onlylove.www.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConsumerSmsTopick {

    private static final String SMS_QUEUE = "sms_queue_topic";
    //交换机名称
    private static final String DESTINATION_NAME = "my_topic_destination";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("短信消费者启动");
        //1建立mq连接
        Connection connection = MQConnectionUtils.newConnection();

        //2创建通道
        Channel channel = connection.createChannel();

        //3消费申明队列
        channel.queueDeclare(SMS_QUEUE, false, false, false, null);

        channel.queueBind(SMS_QUEUE, DESTINATION_NAME, "log.*");


        //5消费监听消息
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("短信消费者获取生产消息"+msg);

            }
        };
        channel.basicConsume(SMS_QUEUE, true, defaultConsumer);

    }

}