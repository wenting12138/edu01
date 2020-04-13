package com.xc.manager_cms.controller;

import com.xc.api.cms.CmsConfigControllerApi;
import com.xc.manager_cms.service.PageService;
import com.xc.model.cms.CmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/config")
@CrossOrigin
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    private PageService pageService;

    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig findById(@PathVariable("id") String id) {
        return pageService.selectById(id);
    }
}
