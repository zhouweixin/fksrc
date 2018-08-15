package com.hnu.fk.repository;

import com.hnu.fk.domain.ActionLog;
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
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    /**
     * 通过时间段查询
     * @param startDate
     * @param endDate
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM permission_action_log where time>=?1 and time <=Dateadd(mm,1,?2) order by time desc", nativeQuery = true)
    public Page<ActionLog> findByTimeLike(String startDate,String endDate,Pageable pageable);
}
