package com.xc.model.learning.response;

import com.xc.ommon.model.response.ResponseResult;
import com.xc.ommon.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class GetMediaResult extends ResponseResult {

    // 视频播放地址
    String fileUrl;

    public GetMediaResult(ResultCode resultCode, String fileUrl) {
        super(resultCode);
        this.fileUrl = fileUrl;
    }

}
