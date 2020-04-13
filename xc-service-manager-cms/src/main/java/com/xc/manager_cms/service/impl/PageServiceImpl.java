package com.xc.manager_cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xc.cmsclient.config.RabbitMqConfig;
import com.xc.manager_cms.dao.*;
import com.xc.manager_cms.service.PageService;
import com.xc.model.cms.CmsConfig;
import com.xc.model.cms.CmsPage;
import com.xc.model.cms.CmsSite;
import com.xc.model.cms.CmsTemplate;
import com.xc.model.cms.request.QueryPageRequest;
import com.xc.model.cms.response.CmsCode;
import com.xc.model.cms.response.CmsPageResult;
import com.xc.model.cms.response.CmsPostPageResult;
import com.xc.model.system.SysDictionary;
import com.xc.ommon.exception.ExceptionCast;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.QueryResult;
import com.xc.ommon.model.response.ResponseResult;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest request) {
        if (request == null) {
            request = new QueryPageRequest();
        }
        if (page <= 0) {
            page = 1;
        }
        if (size <=0) {
            size = 10;
        }
        // 分页查询
        Pageable pageable = PageRequest.of(page, size);
        // 自定义条件查询
        // 定义条件匹配器
//        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
//        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 条件值对象
        CmsPage cmsPage = new CmsPage();
        if (StringUtils.isNotEmpty(request.getSiteId())) {
            // 设置站点id作为查询条件
            cmsPage.setSiteId(request.getSiteId());
        }
        if (StringUtils.isNotEmpty(request.getTemplateId())) {
            // 设置模板id作为查询条件
            cmsPage.setTemplateId(request.getTemplateId());
        }
        if (StringUtils.isNotEmpty(request.getPageAlias())) {
            // 设置模板id作为查询条件
            cmsPage.setPageAliase(request.getPageAlias());
        }
//        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        Page<CmsPage> cmsPages = cmsPageRepository.findAll(pageable);

        QueryResult queryResult = new QueryResult();
        if (cmsPages != null) {
            queryResult.setTotal(cmsPages.getTotalPages());
            queryResult.setList(cmsPages.getContent());
        }
        QueryResponseResult queryPageRequest = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryPageRequest;
    }

    @Override
    public CmsPageResult insert(CmsPage cmsPage) {
        if (cmsPage == null) {
            // 非法参数异常
            ExceptionCast.cast(CommonCode.FAIL);
        }
        // 校验页面名称 站点id 页面webpath的唯一性
        CmsPage page = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(page != null){
            // 页面已存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        cmsPage = cmsPageRepository.insert(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    @Override
    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage byId = this.findById(id);
        if (byId == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        byId.setTemplateId(cmsPage.getTemplateId());
        byId.setSiteId(cmsPage.getSiteId());
        byId.setPageAliase(cmsPage.getPageAliase());
        byId.setPageName(cmsPage.getPageName());
        byId.setPageWebPath(cmsPage.getPageWebPath());
        byId.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
        // 更新dataUrl
        byId.setDataUrl(cmsPage.getDataUrl());
        CmsPage insert = cmsPageRepository.save(byId);



        return new CmsPageResult(CommonCode.SUCCESS, insert);
    }

    @Override
    public CmsPage findById(String id) {
        Optional<CmsPage> repository = cmsPageRepository.findById(id);
        if (repository.isPresent()) {
            CmsPage cmsPage = repository.get();
            return cmsPage;
        }
        return null;
    }

    @Override
    public ResponseResult deleteById(String id) {
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        if (byId.isPresent()) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Autowired
    private CmsCOnfigRepository cmsCOnfigRepository;

    @Override
    public CmsConfig selectById(String id) {
        Optional<CmsConfig> byId = cmsCOnfigRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    // 页面静态化
    @Override
    public String getPageHtml(String pageId) {

        // 静态化程序获取页面的DataUrl
        // 静态化程序远程请求DataUrl获取数据模型
        Map map = getModelByPageId(pageId);
        if (map == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        // 静态化程序获取页面的模板信息
        String content = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(content)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        // 执行页面静态化
        String html = generateHtml(content, map);
        return html;
    }

    @Override
    public ResponseResult post(String pageId) {
        // 执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        // 将页面静态化文件存储到GridFs
        CmsPage cmsPage = saveHtml(pageHtml, pageId);
        // 向mq发消息
        sendPostpage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;
    @Override
    public SysDictionary findBydType(String dType) {

        SysDictionary type = sysDictionaryRepository.findByDType(dType);
        return type;
    }

    @Override
    public CmsPageResult save(CmsPage cmsPage) {
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        CmsPage tem = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (tem != null) {
            // 更新
            return this.update(tem.getPageId(), cmsPage);
        }
        // 添加
        return this.insert(cmsPage);
    }

    @Override
    // 一键发布
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {

        // 1. 保存信息到cms_page
        CmsPageResult cmsPageResult = this.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPage page = cmsPageResult.getCmsPage();
        if (page == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String pageId = page.getPageId();
        // 2. 执行页面静态化
        ResponseResult result = this.post(pageId);// 需要页面id
        if (!result.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        // 拼接页面url
        String siteId = page.getSiteId();
        CmsSite cmsSite = findSiteById(siteId);
        if (cmsSite == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String yuming = cmsSite.getSiteDomain();
        String pageUrl = yuming + cmsSite.getSiteWebPath() + page.getPageWebPath() + page.getPageName();

        return new CmsPostPageResult(CommonCode.SUCCESS, pageUrl);
    }

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    private CmsSite findSiteById(String siteId) {
        Optional<CmsSite> optionalCmsSite = cmsSiteRepository.findById(siteId);
        if (optionalCmsSite.isPresent()) {
            return optionalCmsSite.get();
        }
        return null;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private void sendPostpage(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        CmsPage cmsPage = null;
        if (byId.isPresent()) {
            cmsPage = byId.get();
        }
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        String siteId = cmsPage.getSiteId();
        Map<String, String> map = new HashMap<>();
        map.put("pageId", pageId);
        String msg = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE,siteId, msg);
    }

    private CmsPage saveHtml(String pageHtml, String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        CmsPage cmsPage = null;
        if (byId.isPresent()) {
            cmsPage = byId.get();
        }
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        String pageName = cmsPage.getPageName();
        ObjectId objectId = null;
        InputStream is = null;
        try {
            is = IOUtils.toInputStream(pageHtml, "utf-8");
            objectId = gridFsTemplate.store(is, pageName);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    private String generateHtml(String template, Map map) {
        // 创建一个配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 创建一个模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", template);
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            Template configurationTemplate = configuration.getTemplate("template");
            // 调用方法进行静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(configurationTemplate, map);
            return content;
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        // 获取页面的模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> template = cmsTemplateRepository.findById(templateId);
        if (template.isPresent()) {
            String templateFileId = template.get().getTemplateFileId();
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            // 打开一个下载流
            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // 获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, downloadStream);
            // 从流中获取数据
            try{
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private Map getModelByPageId(String pageId) {
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            // 页面dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        // 通过restTemplate 请求 dataUrl获取数据
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        Map map = entity.getBody();
        return map;
    }
}
