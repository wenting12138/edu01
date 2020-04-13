package com.xc.order.mq;

import com.xc.model.task.XcTask;
import com.xc.order.config.RabbitMQConfig;
import com.xc.order.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
@Slf4j
public class ChooseCourseTask {


    //    @Scheduled(cron = "0/3 * * * * *")
//    @Scheduled(fixedRate = 3000) // 在任务开始后3秒执行下一次调度
//    @Scheduled(fixedDelay = 3000)  // 在任务结束后,三秒后开启执行下一次
    /**
     *  秒  分钟  小时  月的天   月   周中的天
     *   / : 表示指定数值的数量
     *   * : 表示所有可能的值
     *   - : 表示区间范围
     *   , : 表示列举
     *   ? : 仅同于月中的天 和 周中的天两个子表达式, 表示不指定值
     */
    public void test() {

        log.info("[=========================定时任务start]");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("[========================== 定时任务end]");
    }

    @Autowired
    private TaskService taskService;

    @Scheduled(cron = "0/3 * * * * *")
    public void task(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(GregorianCalendar.MINUTE ,-1);
        Date time = calendar.getTime();
        List<XcTask> xcTasks = taskService.findXcTask(time, 100);
        System.out.println(xcTasks);

        // 调用service, 将添加选课的消息发送
        for (XcTask xcTask : xcTasks ) {
            if (taskService.getTask(xcTask.getId(), xcTask.getVersion()) > 0) {
                String exchange = xcTask.getMqExchange();
                String routingKey = xcTask.getMqRoutingkey();
                taskService.publish(xcTask, exchange, routingKey);
            }
        }
        log.info("******************[结束]*****************");
    }

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void receiveFinishChooseCourseTask(XcTask xcTask){
        log.info("[接收到消息:], {}", xcTask);
        if (xcTask != null && StringUtils.isNotEmpty(xcTask.getId())) {
            taskService.finishTask(xcTask.getId());
        }
    }

}
