package com.xc.filesystem.service;

import com.xc.model.filesystem.response.UploadFileResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    UploadFileResult upload(MultipartFile multipartFile, String filetag, String businessKey, String metadata);

}
