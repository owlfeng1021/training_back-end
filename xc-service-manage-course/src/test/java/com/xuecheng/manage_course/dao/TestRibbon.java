package com.xuecheng.manage_course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {
    @Autowired
    RestTemplate restTemplate;
    @Test
    public void  rest1(){
        // 然后在那个拦截器里面有出现了关于负载均衡的操作
        // 确定你要获取的服务名称
        String serviceName = "XC-SERVICE-MANAGE-CMS";
        // ribbon客户端从eurekaServer中获取服务列表
//                5ce4be97f09c2f30ccbdfa40
        for (int i = 0; i < 10; i++) {
            ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://"+serviceName+"/cms/page/get/5ce4be97f09c2f30ccbdfa40", Map.class);
            Map body = forEntity.getBody();
            System.out.println(body);
        }
    }

}
