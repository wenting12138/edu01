package com.xc.ucenter.controller;

import com.netflix.discovery.converters.Auto;
import com.xc.api.ucenter.UcenterControllerApi;
import com.xc.model.ucenter.ext.XcUserExt;
import com.xc.model.ucenter.response.JwtResult;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ucenter.service.UserService;
import com.xc.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping("/getuserext")
    public XcUserExt findUserExt(@RequestParam("username") String username) {
        return userService.findByUsername(username);
    }


}
