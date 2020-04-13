package com.xc.api.media;

import com.xc.model.media.request.QueryMediaFileRequest;
import com.xc.model.media.response.CheckChunkResult;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "媒资管理服务接口")
public interface MediaFileControllerApi {

    @ApiOperation("媒资查询")
    QueryResponseResult findList(int page, int size, QueryMediaFileRequest request);


}
