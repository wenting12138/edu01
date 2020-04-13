package com.xc.order.dao;

import com.xc.model.task.XcTask;
import com.xc.model.task.XcTaskHis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis, String> {


}
