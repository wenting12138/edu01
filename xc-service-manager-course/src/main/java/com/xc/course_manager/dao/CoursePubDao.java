package com.xc.course_manager.dao;

import com.xc.model.course.Category;
import com.xc.model.course.CoursePub;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CoursePubDao {

    void save(CoursePub coursePub);
    void update(CoursePub coursePub);
    CoursePub findById(String id);

    List<CoursePub> findAll();

}
