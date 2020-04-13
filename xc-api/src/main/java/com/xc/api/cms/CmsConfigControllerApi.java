package com.xc.api.cms;

import com.xc.model.cms.CmsConfig;
import com.xc.model.cms.CmsPage;
import com.xc.model.cms.request.QueryPageRequest;
import com.xc.model.cms.response.CmsPageResult;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "cms页面管理接口")
public interface CmsConfigControllerApi {

    @ApiOperation("查询信息")
    CmsConfig findById(String id);

}
