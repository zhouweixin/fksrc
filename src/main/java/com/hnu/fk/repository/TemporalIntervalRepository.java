package com.hnu.fk.repository;

import com.hnu.fk.domain.TemporalInterval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/13
 */
public interface TemporalIntervalRepository extends JpaRepository<TemporalInterval,Long> {
    /**
     * 通过统计年份查询，按照月份排序
     * @param statisticalYear
     * @return
     */
    public List<TemporalInterval> findByStatisticalYearOrderByStatisticalMonthAsc(String statisticalYear);

    /**
     * 通过年和月查询
     *
     * @param year
     * @param month
     * @return
     */
    TemporalInterval findFirstByStatisticalYearAndStatisticalMonth(String year, String month);

}
