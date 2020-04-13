package com.xc.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xc.learning.config.RabbitMQConfig;
import com.xc.learning.service.CourseLearningService;
import com.xc.model.task.XcTask;
import com.xc.ommon.model.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class ChooseCourseTask {

    @Autowired
    private CourseLearningService courseLearningService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE)
    public void chooseCourse(XcTask xcTask) {
        log.info("ChooseCourseTask --- chooseCourse  [接收消息]" , xcTask);

        String body = xcTask.getRequestBody();

        Map map = JSON.parseObject(body, Map.class);

        String userId = (String) map.get("userId");
        String courseId = (String) map.get("courseId");
        String valid = (String) map.get("valid");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String startTime = (String) map.get("startTime");

        String endTime = (String) map.get("endTime");
        Date start = null;
        Date end = null;
        try {
            start = format.parse(startTime);
            end = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ResponseResult result = courseLearningService.addChooseCourse(userId, courseId, valid, start, end, xcTask);
        if (result.isSuccess()) {
            log.info("[消息发送], {}", xcTask);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE, RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE, xcTask);

        }

    }

}
