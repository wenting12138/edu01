package com.xc.course_manager.dao;

import com.xc.model.course.Category;
import com.xc.model.course.Teachplan;
import com.xc.model.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeachplanDao {

    TeachplanNode findTeachplanList(String courseId);

    // 根据课程id 和 parentid 查询
    List<Teachplan> findTeachListByCourseIdAndParentId(@Param("courseId") String courseId, @Param("parentId") String parentId);

    void insert(Teachplan teachplan);

    Teachplan findById(String id);
}
