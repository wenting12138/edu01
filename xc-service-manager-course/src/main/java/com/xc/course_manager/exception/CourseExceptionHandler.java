package com.xc.course_manager.exception;

import com.xc.ommon.exception.SelfExceptionHandler;
import com.xc.ommon.model.response.CommonCode;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class CourseExceptionHandler extends SelfExceptionHandler {

//    static {
//        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
//    }



}
