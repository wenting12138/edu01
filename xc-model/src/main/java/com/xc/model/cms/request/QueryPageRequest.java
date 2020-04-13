package com.xc.model.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  封装页面查询的查询条件
 */

@Data
public class QueryPageRequest {

    // 站点id
    @ApiModelProperty("站点id")
    private String siteId;
    // 页面id
    @ApiModelProperty("页面id")
    private String pageId;
    // 页面名称
    @ApiModelProperty("页面名称")
    private String pageName;
    // 别名
    @ApiModelProperty("别名")
    private String pageAlias;
    // 模板id
    @ApiModelProperty("模板id")
    private String templateId;

}
