package com.hnu.fk.repository;

import com.hnu.fk.domain.StandingBookDetail;
import com.hnu.fk.domain.StandingBookHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StandingBookDetailRepository extends JpaRepository<StandingBookDetail,Integer> {
    /**
     * 通过表头删除数据
     */
    void deleteByStandingBookHeader(StandingBookHeader standingBookHeader);

    /**
     * 通过表头查询所有明细
     */
    List<StandingBookDetail> findByStandingBookHeader(StandingBookHeader standingBookHeader);
}
