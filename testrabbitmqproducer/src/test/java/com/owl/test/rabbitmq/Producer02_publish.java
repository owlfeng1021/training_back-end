package com.owl.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer02_publish {
    // 声名一个队列
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

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
        Connection connection = null;
        Channel channel = null;
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

            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            /**
             *  参数明细
             *  1 交换机的名称
             *  2 交换机的类型
             *  fanout ：对应的rabbitmq的工作模式 publish/subscript
             *  direct ： 对应的routing的工作模式
             *  topic ：对应的topics的工作模式
             *  headers： 对应 headers 工作模式
             */
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            // 进行交换机的绑定 和队列的绑定
            /**
             * 参数明细
             * 1 queue 队列名称
             * 2 exchange 交换机名称
             * 3 routingKey 路由key 作用是交换机根据路由key的值将消息转发到指定的队列中 ,在发布订阅模式中协调为空字符串
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");

            /**
             * 1 exchange 交换机 如果不指定就使用默认交换机 使用空字符串
             * 2 routingkey 路由key 交换机根据路由的key来讲消息转发到指定的队列 如果使用默认的交换机 routingkey 设置为队列的名称
             * 3 props 消息的属性d
             * 4 body 消息的内容
             */
            for (int i= 0 ;i<5 ;i++){
                String message = "send inform messages to user";
                channel.basicPublish(EXCHANGE_FANOUT_INFORM, "", null, message.getBytes());
                System.out.println("send to mq" + message);
            }

            // 会话通道 生产者
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
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
