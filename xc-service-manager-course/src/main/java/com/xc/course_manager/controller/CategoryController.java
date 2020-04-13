package com.xc.course_manager.controller;

import com.xc.api.course.CategoryControllerApi;
import com.xc.course_manager.dao.CategoryDao;
import com.xc.course_manager.service.CourseService;
import com.xc.model.course.Category;
import com.xc.model.course.ext.CategoryNode;
import com.xc.model.course.ext.CourseInfo;
import com.xc.model.course.request.CourseListRequest;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.QueryResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class  CategoryController implements CategoryControllerApi {

    @Resource
    private CourseService courseService;


    @Override
    @GetMapping("/list")
    public QueryResponseResult<CourseInfo> findCourseList() {
        String id = "1";
        CategoryNode list = courseService.findCategoryList(id);
        return new QueryResponseResult(CommonCode.SUCCESS, list);
    }
}
