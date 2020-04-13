package com.xc.learning.controller;

import com.xc.api.learning.CourseLearningControllerApi;
import com.xc.learning.service.CourseLearningService;
import com.xc.model.learning.response.GetMediaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/learninhg/course")
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    private CourseLearningService courseLearningService;

    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public GetMediaResult getMedia(@PathVariable("courseId") String courseId,@PathVariable("teachplanId") String teachplanId) {
        return courseLearningService.getMedia(courseId, teachplanId);
    }
}
