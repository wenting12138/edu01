package com.xc.getway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xc.getway.service.LoginService;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginZuulFilter extends ZuulFilter {


    @Override
    public String filterType() {
        /**
         *  1. pre: 转发之前
         *  2. post: 在之后调用
         *  3. error: 处理请求时发生错误调用
         *  4. routing: 在路由请求时调用
         */
        return FilterConstants.PRE_TYPE;
    }


    @Override
    public int filterOrder() {
        // 越小, 越被优先执行
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //  表示这个过滤器有效
        return true;
    }

    @Autowired
    private LoginService loginService;

    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();

        // 得到request
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        // 从cookie获取身份令牌
        String cookie = loginService.getToken2Cookie(request);
        if (StringUtils.isEmpty(cookie)) {
            access_no();
            return null;
        }
        // 从请求头获取jwt令牌
        String header = loginService.getJwt2Header(request);
        if (StringUtils.isEmpty(header)) {
            access_no();
            return null;
        }
        // 从redis取出jwt过期时间
        long expireTime = loginService.getExpire(cookie);
        if (expireTime < 0) {
            access_no();
            return null;
        }
        return null;
    }


    // 拒绝访问
    private void access_no(){

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        requestContext.setSendZuulResponse(false); // 拒绝访问
        // 设置响应代码
        requestContext.setResponseStatusCode(200);
        // 响应信息
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String responseInfo = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(responseInfo);
        response.setContentType("application/json;charset=utf-8");
    }
}
