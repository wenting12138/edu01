package com.xc.manager_cms.controller;

import com.xc.api.cms.CmsSysDirControllerApi;
import com.xc.manager_cms.service.PageService;
import com.xc.model.system.SysDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class CmsSysDirController implements CmsSysDirControllerApi {

    @Autowired
    private PageService pageService;

    @GetMapping("/dictionary/get/{type}")
    @Override
    public SysDictionary findSysDirByType(@PathVariable("type") String type) {
        return pageService.findBydType(type);
    }
}
