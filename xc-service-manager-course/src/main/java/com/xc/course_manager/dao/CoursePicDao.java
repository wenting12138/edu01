package com.xc.course_manager.dao;

import com.xc.model.course.Category;
import com.xc.model.course.CoursePic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoursePicDao {

    CoursePic findById(String id);

    void insert(CoursePic coursePic);
    void update(CoursePic coursePic);
    int delete(String id);

}
