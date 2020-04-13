package com.xc.media_manager.serivce;

import com.xc.model.media.response.CheckChunkResult;
import com.xc.ommon.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface MediaUploadService {


    ResponseResult register(String fileMd5, String fileName, long fileSize, String mimetype, String fileExt);

    CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize);

    ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk);

    ResponseResult mergechunks(String fileMd5, String fileName, long fileSize, String mimetype, String fileExt);

    ResponseResult sendProcessVideoMsg(String mediaId);
}
