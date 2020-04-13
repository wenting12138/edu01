package com.xc.course_manager.dao;

import com.xc.model.course.Category;
import com.xc.model.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryDao {

    Category findById(String id);

    CategoryNode findList(String id);

}
