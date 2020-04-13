package com.xc.ucenter.dao;

import com.xc.model.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XcMenuMapper {


    // 根据用户id查询用户权限
    List<XcMenu> selectPermissionByUserId(String userId);

}
