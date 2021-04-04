package com.onlylove.www.rabbitmq;

import com.rabbitmq.client.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sun.dc.pr.PRError;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConsumerEmailFanout {

    private static final String EMAIL_QUEUE = "email_queue_fanout";
    //交换机名称
    private static final String DESTINATION_NAME = "my_fanout_destination";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("邮件消费者启动");
        //1建立mq连接
        Connection connection = MQConnectionUtils.newConnection();

        //2创建通道
        Channel channel = connection.createChannel();

        //3消费申明队列
        channel.queueDeclare(EMAIL_QUEUE, false, false, false, null);

        //4消费者队列绑定交换机
        channel.queueBind(EMAIL_QUEUE, DESTINATION_NAME, "");
        //5消费监听消息
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("邮件消费者获取生产消息" + msg);

            }
        };
        channel.basicConsume(EMAIL_QUEUE, true, defaultConsumer);

    }

}
