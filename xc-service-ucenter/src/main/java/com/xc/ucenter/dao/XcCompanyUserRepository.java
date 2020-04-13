package com.xc.ucenter.dao;

import com.xc.model.ucenter.XcCompany;
import com.xc.model.ucenter.XcCompanyUser;
import com.xc.model.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser, String> {

    // 根据userid查
    XcCompanyUser findByUserId(String userId);

}
