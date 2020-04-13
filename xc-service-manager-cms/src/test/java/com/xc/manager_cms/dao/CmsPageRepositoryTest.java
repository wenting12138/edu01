package com.xc.manager_cms.dao;

import com.xc.manager_cms.ManagerCmsMain;
import com.xc.model.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerCmsMain.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void test(){
        CmsPage pageName = cmsPageRepository.findByPageName("index.html");
        System.out.println(pageName);
    }

}