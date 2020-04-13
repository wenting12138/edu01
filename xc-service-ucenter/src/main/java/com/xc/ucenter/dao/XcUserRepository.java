package com.xc.ucenter.dao;

import com.xc.model.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XcUserRepository extends JpaRepository<XcUser, String> {

    // 根据账号 查询 用户
    XcUser findByUsername(String username);

}
