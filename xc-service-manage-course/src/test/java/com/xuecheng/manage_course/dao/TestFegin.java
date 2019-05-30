package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
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
public class TestFegin {
    @Autowired
    CmsPageClient cmsPageClient;// 接口代理对象 由feign生成代理对象
    @Test
    public void  rest1(){
        for (int i = 0; i < 10; i++) {
            CmsPage cmsPageById = cmsPageClient.findCmsPageById("5ce4be97f09c2f30ccbdfa40");
            System.out.println(cmsPageById);
        }
    }

}
