package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialConsumptionHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 12:54 2018/8/21
 * @Modified By:
 */
public interface MaterialConsumptionHeaderRepository extends JpaRepository<MaterialConsumptionHeader, Long> {
    /**
     * 通过日期区间查询
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<MaterialConsumptionHeader> findByDateBetween(Date startDate, Date endDate);

    /**
     * 通过日期区间查询-分页
     *
     * @param startDate
     * @param endDate
     * @return
     */
    Page<MaterialConsumptionHeader> findByDateBetween(Date startDate, Date endDate, Pageable pageable);

    /**
     * 通过日期判断是否已经存在
     *
     * @param date
     * @return
     */
    MaterialConsumptionHeader findFirstByDate(Date date);
}
