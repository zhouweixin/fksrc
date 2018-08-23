package com.hnu.fk.repository;

import com.hnu.fk.domain.DataDictionary;
import com.hnu.fk.domain.StandingBookHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface StandingBookHeaderRepository extends JpaRepository<StandingBookHeader,Integer> {
    /**
     * 根据日期查询
     */
    List<StandingBookHeader> findByDateBetween(Date startDate, Date endDate);
    /**
     * 根据日期查询-分页
     */
    Page<StandingBookHeader> findByDateBetween(Date startDate, Date endDate, Pageable pageable);
    /**
     * 根据日期和班次查询
     */
    List<StandingBookHeader> findByDataDictionary_IdAndDateBetween(Long scheduleId, Date startDate, Date endDate);
    StandingBookHeader findFirstByDateAndDataDictionary(Date date, DataDictionary schedule);
    /**
     * 根据日期和班次查询-分页
     */
    Page<StandingBookHeader> findByDataDictionary_IdAndDateBetween(Long scheduleId, Date startDate, Date endDate, Pageable pageable);
    /**
     * 根据ids查询
     * @param ids
     */
    List<StandingBookHeader> findByIdIn(List<Object> ids);
}
