package com.xc.course_manager.controller;

import com.xc.api.course.TeachplanControllerApi;
import com.xc.course_manager.service.CourseService;
import com.xc.model.course.*;
import com.xc.model.course.ext.CategoryNode;
import com.xc.model.course.ext.CourseInfo;
import com.xc.model.course.ext.CourseView;
import com.xc.model.course.ext.TeachplanNode;
import com.xc.model.course.request.CourseListRequest;
import com.xc.model.course.response.CoursePublishResult;
import com.xc.ommon.exception.ExceptionCast;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;
import com.xc.ommon.web.BaseController;
import com.xc.utils.XcOauth2Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@CrossOrigin
public class CourseController extends BaseController implements TeachplanControllerApi{

    @Autowired
    private CourseService courseService;

    @Override
    // SecurityExpressionRoot类
    // 方法授权控制
    // 用户拥有course_get_baseinfo的权限才可以访问
    @PreAuthorize("hasAuthority('course_get_baseinfo')")
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        if (StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID);
        }
        return courseService.findTeachplanList(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.insert(teachplan);
    }

    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page,
                                                          @PathVariable("size") int size,
                                                          @RequestBody(required = false) CourseListRequest courseListRequest) {
        // 拿到jwt 拿到companyId

        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt jwt = xcOauth2Util.getUserJwtFromHeader(request);
        String companyId = jwt.getCompanyId();
        return courseService.findCourse(companyId, page, size, courseListRequest);
    }

    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourse(@RequestBody CourseBase courseBase) {
        return courseService.insertCourse(courseBase);
    }

    @Override
    @GetMapping("/courseview/{courseId}")
    public CourseBase getCourseById(@PathVariable("courseId") String courseId) {
        return courseService.findById(courseId);
    }

    @Override
    @PostMapping("/coursebase/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id,@RequestBody CourseBase courseBase) {
        return courseService.update(id, courseBase);
    }

    @Override
    @GetMapping("/coursemarket/get/{id}")
    public CourseMarket findCourseMarketById(@PathVariable("id") String id) {
        return courseService.findCourseMarketById(id);
    }

    @Override
    @PostMapping("/coursemarket/update/{id}")
    public ResponseResult updateCourseMarket(@PathVariable(value = "id", required = false)String id, @RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(id, courseMarket);
    }

    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic")String pic) {
        return courseService.addCoursePic(courseId, pic);
    }

    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePicList(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePic(courseId);
    }

    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("/courseviews/{id}")
    public CourseView courseView(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult coursePub(@PathVariable("id") String id) {
        return courseService.publish(id);
    }

    @Override
    @PostMapping("/savemedia")
    public ResponseResult saveMedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.saveMedia(teachplanMedia);
    }
}
