package com.xc.media_manager.controller;

import com.xc.api.media.MediaFileControllerApi;
import com.xc.media_manager.serivce.MediaFileService;
import com.xc.model.media.request.QueryMediaFileRequest;
import com.xc.ommon.model.response.QueryResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {

    @Autowired
    private MediaFileService mediaFileService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryMediaFileRequest request) {
        return mediaFileService.findList(page, size, request);
    }
}
