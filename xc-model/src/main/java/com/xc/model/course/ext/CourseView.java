package com.xc.model.course.ext;

import com.xc.model.course.CourseBase;
import com.xc.model.course.CourseMarket;
import com.xc.model.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
public class CourseView implements Serializable {

    private CourseBase courseBase;  // 基础信息
    private CoursePic coursePic;    // 课程图片
    private CourseMarket courseMarket; // 营销信息
    private TeachplanNode teachplanNode;  // 课程列表

}
