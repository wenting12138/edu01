package com.xc.learning.dao;

import com.xc.model.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XcLearningCourseRepository extends JpaRepository<XcLearningCourse, String> {

    // 根据用户id和课程id查询
    XcLearningCourse findByCourseIdAndUserId(String courseId, String userId);

}
