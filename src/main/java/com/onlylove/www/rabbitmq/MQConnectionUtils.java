package com.onlylove.www.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MQConnectionUtils {


    //创建新的mq连接
    public static Connection newConnection() throws IOException, TimeoutException {

        //1创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2设置连接地址
        factory.setHost("127.0.0.1");
        //3设置用户名cheng
        factory.setUsername("guest");
        //4设置用户密码
        factory.setPassword("guest");

        //5设置端口号
//        factory.setPort(15672);
        //6设置virtualhost地址
//        factory.setVirtualHost("/");
        return factory.newConnection();

    }


}
