package com.xc.learning.service.impl;

import com.xc.learning.dao.XcLearningCourseRepository;
import com.xc.learning.dao.XcTaskHisRepository;
import com.xc.learning.feignclient.SearchFeignClient;
import com.xc.learning.service.CourseLearningService;
import com.xc.model.course.TeachplanMediaPub;
import com.xc.model.learning.LearningCode;
import com.xc.model.learning.XcLearningCourse;
import com.xc.model.learning.response.GetMediaResult;
import com.xc.model.task.XcTask;
import com.xc.model.task.XcTaskHis;
import com.xc.ommon.exception.ExceptionCast;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class CourseLearningServiceImpl implements CourseLearningService {

    @Autowired
    private SearchFeignClient searchFeignClient;

    @Autowired
    private XcLearningCourseRepository xcLearningCourseRepository;

    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;

    @Override
    public GetMediaResult getMedia(String courseId, String teachplanId) {

        // TODO 校验学习权限

        // 获取课程学习地址(视频播放地址)
        TeachplanMediaPub teachplanMediaPub = searchFeignClient.findMediaById(teachplanId);
        if (teachplanMediaPub == null) {
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        String fileUrl = teachplanMediaPub.getMediaUrl();

        return new GetMediaResult(CommonCode.SUCCESS, fileUrl);
    }

    @Override
    @Transactional
    public ResponseResult addChooseCourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask) {
        checkParams(userId, courseId, xcTask);
        // 添加选课
        XcLearningCourse learningCourse = xcLearningCourseRepository.findByCourseIdAndUserId(courseId, userId);
        if (learningCourse != null) {
            // 更新
            learningCourse.setStartTime(startTime);
            learningCourse.setEndTime(endTime);
            learningCourse.setStatus("501001");
            learningCourse.setValid(valid);
            xcLearningCourseRepository.save(learningCourse);
        }else {
            // 添加
            learningCourse = new XcLearningCourse();
            learningCourse.setStartTime(startTime);
            learningCourse.setEndTime(endTime);
            learningCourse.setStatus("501001");
            learningCourse.setValid(valid);
            learningCourse.setUserId(userId);
            learningCourse.setCourseId(courseId);
            xcLearningCourseRepository.save(learningCourse);
        }

        // 向历史记录表插入
        Optional<XcTaskHis> optionalXcTaskHis = xcTaskHisRepository.findById(userId);
        if (!optionalXcTaskHis.isPresent()) {
            // 添加历史任务
            XcTaskHis taskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask, taskHis);
            xcTaskHisRepository.save(taskHis);
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }

    private void checkParams(String userId, String courseId, XcTask xcTask) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(courseId)) {
            log.warn("********[CourseLearningServiceImpl->  checkParams (error)]**********");
            ExceptionCast.cast(CommonCode.INVALID);
        }

        if (xcTask == null || StringUtils.isEmpty(xcTask.getId())) {
            log.warn("********[CourseLearningServiceImpl->  checkParams (error)]*************");
            ExceptionCast.cast(CommonCode.INVALID);
        }
    }
}
