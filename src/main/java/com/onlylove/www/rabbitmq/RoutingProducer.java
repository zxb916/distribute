package com.onlylove.www.rabbitmq;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RoutingProducer {

    //交换机名称
    private static final String DESTINATION_NAME = "my_routing_destination";


    public static void main(String[] args) throws IOException, TimeoutException {


        //1，建立mq连接
        Connection connection = MQConnectionUtils.newConnection();

        //2 创建通道
        Channel channel = connection.createChannel();

        //3 生产者绑定交换机 参数一交换机名称 2交换机类型
        channel.exchangeDeclare(DESTINATION_NAME, "direct");
        //routingKey
        String routingKey = "sms";
        //创建对应的消息
        String msg = "my_routing_destination_msg";
        System.out.println("生产者投递消息");
        //5发送消息
        channel.basicPublish(DESTINATION_NAME, routingKey, null, msg.getBytes());


        //关闭通和连接
        channel.close();
        connection.close();
    }


}
