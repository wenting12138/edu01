package com.xc.media_manager.controller;

import com.xc.api.media.MediaUploadControllerApi;
import com.xc.media_manager.serivce.MediaUploadService;
import com.xc.model.media.response.CheckChunkResult;
import com.xc.ommon.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    private MediaUploadService mediaUploadService;

    @Override
    @PostMapping("/register")
    public ResponseResult register(String fileMd5, String fileName, long fileSize, String mimetype, String fileExt) {
        return mediaUploadService.register(fileMd5,fileName, fileSize, mimetype, fileExt);
    }

    @Override
    @PostMapping("/checkchunk")
    public CheckChunkResult check(String fileMd5, Integer chunk, Integer chunkSize) {
        return mediaUploadService.checkchunk(fileMd5, chunk, chunkSize);
    }

    @Override
    @PostMapping("/uploadchunk")
    public ResponseResult fileUpload(MultipartFile file, String fileMd5, Integer chunk) {
        return mediaUploadService.uploadchunk(file, fileMd5, chunk);
    }

    @Override
    @PostMapping("/mergechunks")
    public ResponseResult merge(String fileMd5, String fileName, long fileSize, String mimetype, String fileExt) {
        return mediaUploadService.mergechunks(fileMd5, fileName, fileSize, mimetype, fileExt);
    }
}
