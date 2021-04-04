package com.onlylove.www.rabbitmq.amqp;


import com.onlylove.www.rabbitmq.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    //交换机名称
    private static final String QUEUE_NAME = "test_queue";


    public static void main(String[] args) throws IOException, TimeoutException {


        //1，建立mq连接
        Connection connection = MQConnectionUtils.newConnection();

        //2 创建通道
        Channel channel = connection.createChannel();

        //创建队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);


        //创建对应的消息
        String msg = "test_hello";
        System.out.println("生产者投递消息" + msg);

        try {
            //开启事务
            channel.txSelect();
            //5发送消息
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            int i = 1 / 0;
            channel.txCommit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("事务已回滚");
            channel.txRollback();
        }
        //关闭通和连接
        channel.close();
        connection.close();
    }


}
