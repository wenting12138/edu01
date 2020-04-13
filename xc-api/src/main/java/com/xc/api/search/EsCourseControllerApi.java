package com.xc.api.search;

import com.xc.model.course.CoursePub;
import com.xc.model.course.TeachplanMediaPub;
import com.xc.model.search.CourseSearchParam;
import com.xc.ommon.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

@Api(value = "搜索服务接口")
public interface EsCourseControllerApi {

    @ApiOperation("课程搜索")
    QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam);

    @ApiOperation("根据id查询")
    Map<String, CoursePub> getdetail(String id);

    @ApiOperation("根据课程计划id查询课程媒资信息")
    TeachplanMediaPub findMediaById(String teachplanId);
}
