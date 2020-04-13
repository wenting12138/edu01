package com.xc.order.service;

import com.xc.model.task.XcTask;

import java.util.Date;
import java.util.List;

public interface TaskService {

    // 查询前n条任务
    List<XcTask> findXcTask(Date updateTime, int size);

    void publish(XcTask xcTask, String exchange, String routingKey);

    int getTask(String id, int version);

    void finishTask(String taskId);
}
