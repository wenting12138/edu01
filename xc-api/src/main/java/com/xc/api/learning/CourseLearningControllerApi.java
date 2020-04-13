package com.xc.api.learning;


import com.xc.model.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value = "录播课程学习管理")
public interface CourseLearningControllerApi {

    @ApiOperation("获取课程学习视频地址")
    GetMediaResult getMedia(String courseId, String teachplanId);
}
