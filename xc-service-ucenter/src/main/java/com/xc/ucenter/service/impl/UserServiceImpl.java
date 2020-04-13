package com.xc.ucenter.service.impl;

import com.xc.model.ucenter.XcCompanyUser;
import com.xc.model.ucenter.XcMenu;
import com.xc.model.ucenter.XcUser;
import com.xc.model.ucenter.ext.XcUserExt;
import com.xc.ucenter.dao.XcCompanyUserRepository;
import com.xc.ucenter.dao.XcMenuMapper;
import com.xc.ucenter.dao.XcUserRepository;
import com.xc.ucenter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private XcUserRepository xcUserRepository;
    @Autowired
    private XcCompanyUserRepository xcCompanyUserRepository;

    @Autowired
    private XcMenuMapper xcMenuMapper;

    @Override
    public XcUserExt findByUsername(String username) {

        XcUser xcUser = xcUserRepository.findByUsername(username);
        if (xcUser == null) {
            return null;
        }
        String userId = xcUser.getId();
        XcCompanyUser companyUser = xcCompanyUserRepository.findByUserId(userId);
        String companyId = null;
        if (companyUser != null) {
            companyId = companyUser.getCompanyId();
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);
        xcUserExt.setCompanyId(companyId);
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
        xcUserExt.setPermissions(xcMenus);
        return xcUserExt;
    }

}
