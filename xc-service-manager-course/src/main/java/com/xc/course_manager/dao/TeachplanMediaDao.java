package com.xc.course_manager.dao;

import com.xc.model.course.Category;
import com.xc.model.course.TeachplanMedia;
import com.xc.model.course.TeachplanMediaPub;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachplanMediaDao {

    TeachplanMedia findById(String id);

    void save(TeachplanMedia teachplanMedia);

    void update(TeachplanMedia teachplanMedia);

    // 根据课程id查询列表
    List<TeachplanMedia> findByCourseId(String courseId);
}

