package com.xc.course_manager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class CoursePublishConfigParams {

    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String publish_previewUrl;
    @Value("${course-publish.pageWebPath}")
    private String publish_pageWebPath;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_pagePhysicalPath;
    @Value("${course-publish.dataUrl}")
    private String publish_dataUrl;

}
