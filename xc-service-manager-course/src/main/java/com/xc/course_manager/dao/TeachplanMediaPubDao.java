package com.xc.course_manager.dao;

import com.xc.model.course.TeachplanMedia;
import com.xc.model.course.TeachplanMediaPub;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachplanMediaPubDao {

    long deleteByCourseId(String courseId);

    void saveAll(List<TeachplanMediaPub> teachplanMediaPubs);

    List<TeachplanMediaPub> findAll();

}

