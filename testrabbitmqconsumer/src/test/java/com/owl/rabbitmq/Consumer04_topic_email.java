package com.owl.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Consumer04_topic_email {
    // 声名一个队列
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    private static final String ROUTINGKEY_EMALL = "infrom.#.email.#";
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
        try {
            connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            /**
             * 声明交换机
             * 参数明细
             * 1 交换机名称
             * 2 交换机类型 fanout topic direct headers
             *
             */
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM,BuiltinExchangeType.TOPIC);
            /**
             *  监听队列
             *  声明队列
             *  参数明细
             *  1 queue 队列名称
             *  2 durable 是否持久化
             *  3 exclusive是否独占连接
             *  4 autoDelete 删除闲置队列
             *  5 arguments 扩展参数 如（设置存活时间）
             */

            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            /**
             * 绑定队列
             * 参数明细
             * 1 队列名称
             * 2 交换机名称
             * 3 路由key
             *
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_EMALL);
            // 打印的执行方法
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                /**
                 *  当接收到消息的后此方法将被调用
                 * @param consumerTag 消费者标签 用来标识 消费者 的 在监听队列设置为 channel.basicConsume
                 * @param envelope 信封
                 * @param properties 消息属性
                 * @param body 消息内容
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    // 交换机
                    String exchange = envelope.getExchange();
                    // 标识消息的id 用于确定信息已经接收
                    long deliveryTag = envelope.getDeliveryTag();

                    String message = new String(body, "utf-8");
                    System.out.println("messages:" + message);
                }
            };
            /**
             *  参数明细
             *  1 queue 队列名称
             *  2 autoAck 自动回复 当消费者收到信息后告诉消息已经接收 如果设置为true表示会自动回复 mq 如果设置为false要通过编程手动实现回复
             *  3 callback  消费方法 当消费者收到消息 要执行的方法
             */
            channel.basicConsume(QUEUE_INFORM_EMAIL, true, defaultConsumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();

        }
    }
}
