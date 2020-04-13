package com.xc.media_manager.serivce.impl;

import com.xc.media_manager.dao.MediaFileDao;
import com.xc.media_manager.serivce.MediaFileService;
import com.xc.model.media.MediaFile;
import com.xc.model.media.request.QueryMediaFileRequest;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    private MediaFileDao mediaFileDao;


    @Override
    public QueryResponseResult findList(int page, int size, QueryMediaFileRequest request) {
        if (page < 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        if (request == null) {
            request = new QueryMediaFileRequest();
        }
        MediaFile mediaFile = new MediaFile();
        if (StringUtils.isNotEmpty(request.getTag())) {
            mediaFile.setFileStatus(request.getTag());
        }
        if (StringUtils.isNotEmpty(request.getFileOriginalName())) {
            mediaFile.setFileStatus(request.getFileOriginalName());
        }
        if (StringUtils.isNotEmpty(request.getProcessStatus())) {
            mediaFile.setProcessStatus(request.getProcessStatus());
        }
        // 条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("processStatus", ExampleMatcher.GenericPropertyMatchers.exact());

        // 自定义条件查询
        Example<MediaFile> example = Example.of(mediaFile, exampleMatcher);
        Pageable pageable = PageRequest.of(page, size);
        Page<MediaFile> mediaFiles = mediaFileDao.findAll(example, pageable);
        // 总记录数
        long total = mediaFiles.getTotalPages();
        // 数据列表
        List<MediaFile> list = mediaFiles.getContent();
        QueryResult result = new QueryResult();
        result.setTotal(total);
        result.setList(list);
        return new QueryResponseResult(CommonCode.SUCCESS, result);
    }
}
