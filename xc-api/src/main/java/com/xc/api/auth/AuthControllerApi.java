package com.xc.api.auth;

import com.xc.model.ucenter.request.LoginRequest;
import com.xc.model.ucenter.response.JwtResult;
import com.xc.model.ucenter.response.LoginResult;
import com.xc.ommon.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "用户服务")
public interface AuthControllerApi {

    @ApiOperation("登录")
    LoginResult login(LoginRequest request);

    @ApiOperation("退出")
    ResponseResult layout();

    @ApiOperation("查询jwt令牌")
    JwtResult userjwt();
}
