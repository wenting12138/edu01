package com.xc.api.course;

import com.xc.model.course.*;
import com.xc.model.course.ext.CourseInfo;
import com.xc.model.course.ext.CourseView;
import com.xc.model.course.ext.TeachplanNode;
import com.xc.model.course.request.CourseListRequest;
import com.xc.model.course.response.CoursePublishResult;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("课程接口")
public interface TeachplanControllerApi {

    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询课程列表")
    QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("课程添加")
    ResponseResult addCourse(CourseBase courseBase);

    @ApiOperation("获取课程基础信息")
    CourseBase getCourseById(String courseId);

    @ApiOperation("更新课程基础信息")
    ResponseResult updateCourseBase(String id, CourseBase courseBase);

    @ApiOperation("获取课程营销信息")
    CourseMarket findCourseMarketById(String id);

    @ApiOperation("更新课程营销信息")
    ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("查询课程图片")
    CoursePic findCoursePicList(String courseId);

    @ApiOperation("删除课程图片")
    ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程详细页面查询")
    CourseView courseView(String id);

    @ApiOperation("课程预览")
    CoursePublishResult preview(String id);

    @ApiOperation("课程发布")
    CoursePublishResult coursePub(String id);

    @ApiOperation("保存课程计划和媒资的关联")
    ResponseResult saveMedia(TeachplanMedia teachplanMedia);

}
