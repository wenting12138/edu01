package com.xc.manager_cms.service.impl;

import com.xc.manager_cms.ManagerCmsMain;
import com.xc.manager_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = ManagerCmsMain.class)
@RunWith(SpringRunner.class)
public class PageServiceImplTest {

    @Autowired
    private PageService pageService;

    @Test
    public void getPageHtml() {
        String pageHtml = pageService.getPageHtml("5a754adf6abb500ad05688d9");
        System.out.println(pageHtml);
    }
}