package com.xc.course_manager.service;

import com.xc.model.course.*;
import com.xc.model.course.ext.CategoryNode;
import com.xc.model.course.ext.CourseInfo;
import com.xc.model.course.ext.CourseView;
import com.xc.model.course.ext.TeachplanNode;
import com.xc.model.course.request.CourseListRequest;
import com.xc.model.course.response.CoursePublishResult;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;

public interface CourseService {

    // 课程计划查询
    TeachplanNode findTeachplanList(String courseId);

    // 添加课程计划
    ResponseResult insert(Teachplan teachplan);

    CategoryNode findCategoryList(String id);

    QueryResponseResult<CourseInfo> findCourse(String companyId, int page, int size, CourseListRequest courseListRequest);

    ResponseResult insertCourse(CourseBase courseBase);

    CourseBase findById(String id);

    ResponseResult update(String id, CourseBase courseBase);

    CourseMarket findCourseMarketById(String id);

    ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);

    ResponseResult addCoursePic(String courseId, String pic);

    CoursePic findCoursePic(String courseId);

    ResponseResult deleteCoursePic(String courseId);

    CourseView getCourseView(String id);

    CoursePublishResult preview(String id);

    CoursePublishResult publish(String id);

    ResponseResult saveMedia(TeachplanMedia teachplanMedia);
}
