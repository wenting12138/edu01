package com.xc.course_manager.feignclient;


import com.xc.model.cms.CmsPage;
import com.xc.model.cms.response.CmsPageResult;
import com.xc.model.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "XC-SERVICE-MANAGER-CMS")
public interface CmsPageClient {

    // 添加页面
    @PostMapping("/cms/page/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage);

    // 一键发布
    @PostMapping("/cms/page/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);

}
