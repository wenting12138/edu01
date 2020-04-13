package com.xc.search.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class EsIndexParams {

    @Value("${course.index}")
    private String courseIndex;
    @Value("${course.type}")
    private String courseIndexType;

    @Value("${course.sourse_field}")
    private String feilds;


    @Value("${media.index}")
    private String courseMediaIndex;

    @Value("${media.type}")
    private String mediaIndexType;

    @Value("${media.sourse_field}")
    private String mediaFeilds;
}
