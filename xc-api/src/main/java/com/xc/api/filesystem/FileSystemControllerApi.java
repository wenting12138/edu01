package com.xc.api.filesystem;

import com.xc.model.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api("文件服务接口")
public interface FileSystemControllerApi {

    /**
     * 文件上传
     * @param multipartFile: 文件本身
     * @param filetag: 标签
     * @param businessKey 业务key
     * @param metadata 文件元信息(json)
     * @return
     */
    @ApiOperation("上传文件")
    UploadFileResult upload(MultipartFile multipartFile, String filetag, String businessKey, String metadata);

}
