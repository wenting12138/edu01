package com.xc.model.course.ext;

import com.xc.model.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by admin on 2018/2/7.
 */
@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

    // 媒资文件id
    String mediaId;

    // 媒资文件的原始名称
    String mediaFileOriginalName;

}
