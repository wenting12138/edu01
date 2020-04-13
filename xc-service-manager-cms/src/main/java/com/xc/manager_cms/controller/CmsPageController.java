package com.xc.manager_cms.controller;

import com.xc.api.cms.CmsPageControllerApi;
import com.xc.manager_cms.dao.CmsPageRepository;
import com.xc.manager_cms.service.PageService;
import com.xc.model.cms.CmsPage;
import com.xc.model.cms.request.QueryPageRequest;
import com.xc.model.cms.response.CmsPageResult;
import com.xc.model.cms.response.CmsPostPageResult;
import com.xc.model.system.SysDictionary;
import com.xc.ommon.model.response.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,
                                        @PathVariable("size") int size,
                                        QueryPageRequest request) {
        return pageService.findList(page, size, request);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult insert(@RequestBody CmsPage cmsPage) {
        return pageService.insert(cmsPage);
    }

    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult update(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return pageService.update(id, cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return pageService.findById(id);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
        return pageService.deleteById(id);
    }

    @Resource
    private CmsPageRepository cmsPageRepository;

    @GetMapping("/all")
    public List<CmsPage> getAll(String id){
        return cmsPageRepository.findAll();
    }

    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.post(pageId);
    }

    @Override
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return pageService.save(cmsPage);
    }

    @Override
    @PostMapping("/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
        return pageService.postPageQuick(cmsPage);
    }
}
