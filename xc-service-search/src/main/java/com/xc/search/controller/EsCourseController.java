package com.xc.search.controller;

import com.xc.api.search.EsCourseControllerApi;
import com.xc.model.course.CoursePub;
import com.xc.model.course.TeachplanMediaPub;
import com.xc.model.search.CourseSearchParam;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.QueryResult;
import com.xc.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    private EsCourseService esCourseService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult list(@PathVariable("page") int page,@PathVariable("size") int size,  CourseSearchParam courseSearchParam) {
        return esCourseService.list(page, size, courseSearchParam);
    }

    @Override
    @GetMapping("/getall/{id}")
    public Map<String, CoursePub> getdetail(@PathVariable("id") String id) {
        return esCourseService.getall(id);
    }

    @Override
    @GetMapping("/getmedia/{teachplanId}")
    public TeachplanMediaPub findMediaById(@PathVariable("teachplanId") String teachplanId) {
        String[] teachplans = new String[] {teachplanId};
        QueryResponseResult media = esCourseService.getMediaById(teachplans);
        QueryResult queryResult = media.getQueryResult();
        if (queryResult != null) {
            List<TeachplanMediaPub> list = queryResult.getList();
            if (list != null && list.size() == 1) {
                return list.get(0);
            }

        }
        return new TeachplanMediaPub();
    }
}
