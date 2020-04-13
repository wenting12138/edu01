package com.xc.course_manager.dao;

import com.xc.model.course.Category;
import com.xc.model.course.CourseMarket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseMarketDao {

    CourseMarket findById(String id);

    void insert(CourseMarket courseMarket);

    void update(CourseMarket courseMarket);
}
