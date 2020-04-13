package com.xc.manager_cms.controller;

import com.xc.manager_cms.service.PageService;
import com.xc.ommon.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
@CrossOrigin
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private PageService pageService;

    // 页面预览
    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) throws IOException {
        String pageHtml = pageService.getPageHtml(pageId);
        response.setHeader("Content-type", "text/html;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(pageHtml.getBytes("utf-8"));
    }

}
