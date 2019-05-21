package com.xuecheng.test.freemarker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-12 23:29
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class testRestTemplate {
    @Autowired
    RestTemplate restTemplate;
    @Test
    public void testRestTemplate(){

        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/page/get/5ce36935f09c2f114c9f47fb", Map.class);
        System.out.println(forEntity);
    }
}
