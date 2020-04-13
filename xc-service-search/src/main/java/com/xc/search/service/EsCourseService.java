package com.xc.search.service;

import com.xc.model.course.CoursePub;
import com.xc.model.course.TeachplanMediaPub;
import com.xc.model.search.CourseSearchParam;
import com.xc.ommon.model.response.QueryResponseResult;

import java.util.Map;

public interface EsCourseService {

    QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam);

    Map<String, CoursePub> getall(String id);

    QueryResponseResult getMediaById(String[] teachplanIds);
}
