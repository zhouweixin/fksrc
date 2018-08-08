package com.hnu.fk.repository;

import com.hnu.fk.domain.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
    public Page<LoginLog> findByTimeLike(Date time, Pageable pageable);
}
