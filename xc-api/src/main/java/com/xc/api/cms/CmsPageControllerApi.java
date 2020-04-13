package com.xc.api.cms;

import com.xc.model.cms.CmsPage;
import com.xc.model.cms.request.QueryPageRequest;
import com.xc.model.cms.response.CmsPageResult;
import com.xc.model.cms.response.CmsPostPageResult;
import com.xc.model.system.SysDictionary;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "cms页面管理接口")
public interface CmsPageControllerApi {

    @ApiOperation("分页查询页面列表")
    QueryResponseResult findList(@ApiParam(value = "page", name = "页码", required = true) int page,
                                 @ApiParam(value = "size", name = "页码", required = true) int size,
                                 QueryPageRequest request);

    @ApiOperation("新增站点数据")
    CmsPageResult insert(CmsPage cmsPage);

    @ApiOperation("修改数据")
    CmsPageResult update(String id, CmsPage cmsPage);

    @ApiOperation("查询页面信息")
    CmsPage findById(String id);

    @ApiOperation("删除页面")
    ResponseResult deleteById(String id);

    @ApiOperation("页面发布")
    ResponseResult post(String pageId);

    @ApiOperation("保存页面")
    CmsPageResult save(CmsPage cmsPage);

    @ApiOperation("一键发布课程")
    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
