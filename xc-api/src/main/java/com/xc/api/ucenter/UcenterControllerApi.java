package com.xc.api.ucenter;

import com.xc.model.ucenter.ext.XcUserExt;
import com.xc.model.ucenter.response.JwtResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "用户中心")
public interface UcenterControllerApi {

    @ApiOperation("根据用户账号查询")
    XcUserExt findUserExt(String username);


}
