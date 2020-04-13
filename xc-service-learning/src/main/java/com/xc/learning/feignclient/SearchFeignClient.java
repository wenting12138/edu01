package com.xc.learning.feignclient;

import com.xc.model.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "XC-SERVICE-SEARCH")
public interface SearchFeignClient {

    @GetMapping("/search/course/getmedia/{teachplanId}")
    TeachplanMediaPub findMediaById(@PathVariable("teachplanId") String teachplanId);



}
