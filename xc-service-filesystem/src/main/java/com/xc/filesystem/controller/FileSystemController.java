package com.xc.filesystem.controller;

import com.xc.api.filesystem.FileSystemControllerApi;
import com.xc.filesystem.service.FileService;
import com.xc.model.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {

    @Autowired
    private FileService fileService;

    @Override
    @PostMapping("/upload")
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businessKey, String metadata) {
        return fileService.upload(multipartFile, filetag, businessKey, metadata);
    }
}
