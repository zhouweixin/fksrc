package com.hnu.fk.repository;

import com.hnu.fk.domain.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/7
 */
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    /**
     * 通过日期模糊查询
     * @param time
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM permission_action_log where datediff(day,time,?1)=0 order by time desc ", nativeQuery = true)
    public Page<LoginLog> findByTimeLike(String time,Pageable pageable);}
