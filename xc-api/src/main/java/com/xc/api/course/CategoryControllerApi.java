package com.xc.api.course;

import com.xc.model.course.ext.CourseInfo;
import com.xc.model.course.request.CourseListRequest;
import com.xc.ommon.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("分类查询")
public interface CategoryControllerApi {

    @ApiOperation("三级分类查询")
    QueryResponseResult<CourseInfo> findCourseList();
}
