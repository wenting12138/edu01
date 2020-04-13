package com.xc.model.ucenter.ext;

import com.xc.model.course.ext.CategoryNode;
import com.xc.model.ucenter.XcMenu;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by admin on 2018/3/20.
 */
@Data
@ToString
public class XcMenuExt extends XcMenu {

    List<CategoryNode> children;
}
