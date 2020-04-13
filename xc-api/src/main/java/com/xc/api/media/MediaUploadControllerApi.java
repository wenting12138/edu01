package com.xc.api.media;

import com.xc.model.media.response.CheckChunkResult;
import com.xc.ommon.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "媒资上传服务接口")
public interface MediaUploadControllerApi {

    // 文件上传前的准备工作
    @ApiOperation("文件上传注册接口")
    ResponseResult register(String fileMd5,
                            String fileName,
                            long fileSize,
                            String mimetype,
                            String fileExt);

    @ApiOperation("校验分块文件是否存在")
    CheckChunkResult check(String fileMd5,
                           // 块的序号
                           Integer chunk,
                           // 块的序号
                           Integer chunkSize);

    @ApiOperation("文件上传")
    ResponseResult fileUpload(MultipartFile file,
                              String fileMd5,
                              Integer chunk);

    @ApiOperation("合并分块")
    ResponseResult merge(String fileMd5,
                         String fileName,
                         long fileSize,
                         String mimetype,
                         String fileExt);


}
