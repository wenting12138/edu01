package com.xc.api.cms;

import com.xc.model.cms.CmsPage;
import com.xc.model.cms.request.QueryPageRequest;
import com.xc.model.cms.response.CmsPageResult;
import com.xc.model.system.SysDictionary;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "cms课程接口")
public interface CmsSysDirControllerApi {

    @ApiOperation("课程等级查询")
    SysDictionary findSysDirByType(String type);
}
