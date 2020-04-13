package com.xc.cmsclient.mq;

import com.alibaba.fastjson.JSON;
import com.xc.cmsclient.dao.CmsPageRepository;
import com.xc.cmsclient.service.PageService;
import com.xc.model.cms.CmsPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class ConsumerPostPage {

    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {"${xc.mq.queue}"})
    public void postPage(String pageId){
        log.info("*******[mq接收消息]*********, pageId: {}", pageId);
        Map map = JSON.parseObject(pageId, Map.class);
        pageId = (String) map.get("pageId");
        pageService.savePageToServerPath(pageId);
    }

}
