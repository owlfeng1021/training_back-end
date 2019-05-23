package com.owl.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer01 {
    // 声名一个队列
    private static  final  String QUEUE="hello world";

    public static void main(String[] args) {
        // 连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        // 设置虚拟机 一个mq的服务可以设置多个虚拟机 每一个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        // 和mq建立连接
        Connection connection= null;
        Channel channel =null;
        try {
            // 建立新的连接
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            /**
             *  参数明细
             *  1 queue 队列名称
             *  2 durable 是否持久化
             *  3 exclusive是否独占连接
             *  4 autoDelete 删除闲置队列
             *  5 arguments 扩展参数 如（设置存活时间）
             */
            channel.queueDeclare(QUEUE,true,false,false,null);
            /**
             * 1 exchange 交换机 如果不指定就使用默认交换机 使用空字符串
             * 2 routingkey 路由key 交换机根据路由的key来讲消息转发到指定的队列 如果使用默认的交换机 routingkey 设置为队列的名称
             * 3 props 消息的属性
             * 4 body 消息的内容
             */
            String message="hello world owlfeng";
            channel.basicPublish("",QUEUE,null,message.getBytes());
            System.out.println("send to mq"+message);
            // 会话通道 生产者
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            // 关闭连接 先关闭连接
            try {
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

    }
}
