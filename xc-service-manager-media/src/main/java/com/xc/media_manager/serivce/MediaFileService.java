package com.xc.media_manager.serivce;

import com.xc.model.media.request.QueryMediaFileRequest;
import com.xc.model.media.response.CheckChunkResult;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface MediaFileService {


    QueryResponseResult findList(int page, int size, QueryMediaFileRequest request);
}
