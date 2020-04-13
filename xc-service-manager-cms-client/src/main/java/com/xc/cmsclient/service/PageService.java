package com.xc.cmsclient.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xc.cmsclient.dao.CmsPageRepository;
import com.xc.cmsclient.dao.CmsSiteRepository;
import com.xc.model.cms.CmsPage;
import com.xc.model.cms.CmsSite;
import com.xc.model.cms.response.CmsCode;
import com.xc.ommon.exception.ExceptionCast;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@Slf4j
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    // 保存html页面到服务器物理路径
    public void savePageToServerPath(String pageId){
        // 得到html的文件id, 从cmspage中获取htmlFileId
        CmsPage cmsPage = findCmsPageById(pageId);
        if (cmsPage == null || cmsPage.getHtmlFileId() == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        InputStream is = getFileById(cmsPage.getHtmlFileId());
        if (is == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
            log.error("getFileById InputStream is null, htmlFileId: {}", cmsPage.getHtmlFileId());
            return;
        }
        // 根据站点id 查询站点信息
        CmsSite cmsSite = findCmsSiteById(cmsPage.getSiteId());
        if (cmsSite == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
            log.error("getSiteId CmsSite is null, SiteId: {}", cmsPage.getSiteId());
            return;
        }
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();

        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pagePath);
            IOUtils.copy(is, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private InputStream getFileById(String htmlFileId) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        // 打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try{
            return gridFsResource.getInputStream();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private CmsPage findCmsPageById(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if (byId.isPresent()) {
            CmsPage cmsPage = byId.get();
            if (cmsPage != null) {
                return cmsPage;
            }
        }
        return null;
    }

    private CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> byId = cmsSiteRepository.findById(siteId);
        if (byId.isPresent()) {
            CmsSite cmsSite = byId.get();
            if (cmsSite != null) {
                return cmsSite;
            }
        }
        return null;
    }

}
