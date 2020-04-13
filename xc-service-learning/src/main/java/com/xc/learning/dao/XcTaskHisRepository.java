package com.xc.learning.dao;

import com.xc.model.task.XcTaskHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis, String> {



}
