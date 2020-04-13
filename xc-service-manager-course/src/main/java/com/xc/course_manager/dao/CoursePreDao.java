package com.xc.course_manager.dao;

import com.xc.model.course.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoursePreDao {

    Category findById(String id);

}
