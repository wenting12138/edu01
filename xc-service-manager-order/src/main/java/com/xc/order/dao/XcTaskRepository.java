package com.xc.order.dao;

import com.xc.model.task.XcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
@Repository
public interface XcTaskRepository extends JpaRepository<XcTask, String> {

    // 查询一分钟之前的前n条任务
    Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

    @Modifying
    @Query("update XcTask t set t.updateTime = :updateTime where t.id = :id")
    void updateTaskTime(@Param("id") String id, @Param("updateTime") Date updateTime);

    @Modifying
    @Query("update XcTask t set t.version = :version+1 where t.id = :id and t.version = :version")
    int updateTaskVersion(@Param("id") String id,@Param("version") int version);
}
