package com.xuecheng.manage_cms.dao;

import com.xuecheng.manage_cms.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class testRabbitmqProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void test1() {
        for (int i = 0; i < 50; i++)
            this.rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, "5a751fab6abb5044e0d19ea1", "这是我的消息");
        System.out.println("看看控制台 发送进去没有");
    }
}
