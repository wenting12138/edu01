package com.xc.course_manager.dao;

import com.xc.model.course.Category;
import com.xc.model.course.CourseBase;
import com.xc.model.course.ext.CategoryNode;
import com.xc.model.course.ext.CourseInfo;
import com.xc.model.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseBaseDao {

    CourseBase findById(String id);

    List<CourseInfo> findLimitPage(@Param("page") int page, @Param("size") int size, @Param("request") CourseListRequest courseListRequest);

    void insert(CourseBase courseBase);

    void update(CourseBase courseBase);

    Integer findCount();
}
