package com.xc.test.controller;

import com.xc.test.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class TestController {


    @RequestMapping("/v1")
    public String test1(Map<String, Object> map){
        Student student = new Student();
        student.setName("张三");
        map.put("student", student);
        return "index";
    }

}
