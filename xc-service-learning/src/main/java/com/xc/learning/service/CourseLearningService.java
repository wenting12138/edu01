package com.xc.learning.service;

import com.xc.model.learning.response.GetMediaResult;
import com.xc.model.task.XcTask;
import com.xc.ommon.model.response.ResponseResult;

import java.util.Date;

public interface CourseLearningService {

    GetMediaResult getMedia(String courseId, String teachplanId);

    ResponseResult addChooseCourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask);

}
