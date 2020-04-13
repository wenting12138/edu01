package com.xc.order.service.impl;

import com.netflix.discovery.converters.Auto;
import com.xc.model.task.XcTask;
import com.xc.model.task.XcTaskHis;
import com.xc.order.dao.XcTaskHisRepository;
import com.xc.order.dao.XcTaskRepository;
import com.xc.order.service.TaskService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private XcTaskRepository xcTaskRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;

    @Override
    public List<XcTask> findXcTask(Date updateTime, int size) {

        // 设置分页参数
        Pageable pageable = PageRequest.of(0, size);

        Page<XcTask> xcTaskPages = xcTaskRepository.findByUpdateTimeBefore(pageable, updateTime);

        return xcTaskPages.getContent();

    }

    @Override
    public void publish(XcTask xcTask, String exchange, String routingKey) {

        Optional<XcTask> optionalXcTask = xcTaskRepository.findById(xcTask.getId());
        if (optionalXcTask.isPresent()) {
            // 发送添加选课的消息
            rabbitTemplate.convertAndSend(exchange, routingKey, xcTask);

            // 更新任务的时间
            XcTask task = optionalXcTask.get();
            task.setUpdateTime(new Date());
            xcTaskRepository.save(xcTask);
        }
    }

    @Override
    @Transactional
    public int getTask(String id, int version) {

        int effectRowNum = xcTaskRepository.updateTaskVersion(id, version);

        return effectRowNum;
    }

    @Override
    @Transactional
    public void finishTask(String taskId) {

        Optional<XcTask> optionalXcTask = xcTaskRepository.findById(taskId);
        if (optionalXcTask.isPresent()) {
            // 当前未完成任务
            XcTask xcTask = optionalXcTask.get();
            XcTaskHis taskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask, taskHis);
            // 创建历史任务
            xcTaskHisRepository.save(taskHis);
            // 删除完成任务
            xcTaskRepository.delete(xcTask);
        }

    }
}
