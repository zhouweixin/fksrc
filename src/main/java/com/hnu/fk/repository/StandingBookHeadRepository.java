package com.hnu.fk.repository;

import com.hnu.fk.domain.StandingBookHead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public interface StandingBookHeadRepository extends JpaRepository<StandingBookHead,Integer> {
    /**
     * 根据日期查询
     */
    List<StandingBookHead> findByDateBetween(Date startDate, Date endDate);
    /**
     * 根据日期查询-分页
     */
    Page<StandingBookHead> findByDateBetween(Date startDate, Date endDate, Pageable pageable);
    /**
     * 根据日期和班次查询
     */
    List<StandingBookHead> findBySsbzIdAndDateBetween(Integer bzId,Date startDate,Date endDate);
    /**
     * 根据日期和班次查询-分页
     */
    Page<StandingBookHead> findBySsbzIdAndDateBetween(Integer bzId,Date startDate,Date endDate,Pageable pageable);
}
