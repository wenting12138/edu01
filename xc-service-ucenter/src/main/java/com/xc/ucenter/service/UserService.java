package com.xc.ucenter.service;

import com.xc.model.ucenter.ext.XcUserExt;

public interface UserService {


    XcUserExt findByUsername(String username);
}
