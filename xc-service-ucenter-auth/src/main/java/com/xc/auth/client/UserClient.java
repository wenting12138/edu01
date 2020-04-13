package com.xc.auth.client;

import com.xc.model.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "XC-SERVICE-UCENTER")
public interface UserClient {

    @GetMapping("/ucenter/getuserext")
    XcUserExt findUserExt(@RequestParam("username") String username);

}
