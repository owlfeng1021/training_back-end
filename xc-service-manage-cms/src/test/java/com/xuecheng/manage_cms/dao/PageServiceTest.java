package com.xuecheng.manage_cms.dao;

import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Autowired
    PageService pageService;
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Test
    public  void  testFindAll()
    {
        String pageHtml = pageService.getPageHtml("5ce4be97f09c2f30ccbdfa40");

    }
}
