package com.xc.auth.controller;

import com.xc.api.auth.AuthControllerApi;
import com.xc.auth.service.AuthService;
import com.xc.model.ucenter.ext.AuthToken;
import com.xc.model.ucenter.ext.UserTokenStore;
import com.xc.model.ucenter.request.LoginRequest;
import com.xc.model.ucenter.response.AuthCode;
import com.xc.model.ucenter.response.JwtResult;
import com.xc.model.ucenter.response.LoginResult;
import com.xc.ommon.exception.ExceptionCast;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.ResponseResult;
import com.xc.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class AuthController implements AuthControllerApi {

    @Autowired
    private AuthService authService;
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.cookieDomin}")
    private String cookieDomin;
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;


    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest request) {

        if (request == null || StringUtils.isEmpty(request.getUsername())) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if (request == null || StringUtils.isEmpty(request.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        String username = request.getUsername();

        String password = request.getPassword();

        // 申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

        // 存储到cookie
        String accessToken = authToken.getAccess_token();
        saveToCookie(accessToken);

        return new LoginResult(CommonCode.SUCCESS, accessToken);
    }

    private void saveToCookie(String access_token) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        CookieUtil.addCookie(response, cookieDomin, "/", "uid", access_token, cookieMaxAge, false);
    }

    @Override
    @PostMapping("/userlayout")
    public ResponseResult layout() {
        // 删除redis的jwt
        String token = getTokenToCookie();
        authService.deleleToken(token);
        // 清除cookie
        delCookie(token);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @GetMapping("/userjwt")
    public JwtResult userjwt() {

        // 取出cookie当中的用户身份令牌
        String uid = getTokenToCookie();
        if (uid == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        // 再从redis用身份令牌查询jwt令牌
        AuthToken authToken = authService.findJwt(uid);
        if (authToken != null) return new JwtResult(CommonCode.SUCCESS, authToken.getJwt_token());
        return null;
    }

    private String getTokenToCookie() {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        Map<String, String> cookie = CookieUtil.readCookie(request, "uid");
        if (cookie != null && cookie.get("uid") != null) return cookie.get("uid");

        return null;
    }

    private void delCookie(String access_token){

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        CookieUtil.addCookie(response, cookieDomin, "/", "uid", access_token, 0, false);
    }

}
