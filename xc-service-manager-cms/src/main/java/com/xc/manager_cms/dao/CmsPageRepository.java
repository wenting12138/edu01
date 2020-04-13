package com.xc.manager_cms.dao;

import com.xc.model.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    // 根据页面名称查询
    CmsPage findByPageName(String pageName);

    // 根据页面名称 站点id 页面webpath
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);

}
