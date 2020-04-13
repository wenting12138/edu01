package com.xc.manager_cms.service;

import com.xc.model.cms.CmsConfig;
import com.xc.model.cms.CmsPage;
import com.xc.model.cms.request.QueryPageRequest;
import com.xc.model.cms.response.CmsPageResult;
import com.xc.model.cms.response.CmsPostPageResult;
import com.xc.model.system.SysDictionary;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;

public interface PageService {

    QueryResponseResult findList(int page, int size, QueryPageRequest request);

    CmsPageResult insert(CmsPage cmsPage);

    CmsPageResult update(String id, CmsPage cmsPage);

    CmsPage findById(String id);

    ResponseResult deleteById(String id);

    CmsConfig selectById(String id);

    // 页面静态化的方法
    String getPageHtml(String pageId);

    ResponseResult post(String pageId);

    SysDictionary findBydType(String dType);

    CmsPageResult save(CmsPage cmsPage);

    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
