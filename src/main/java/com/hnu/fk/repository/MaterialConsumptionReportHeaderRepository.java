package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialConsumptionReportHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 13:24 2018/8/22
 * @Modified By:
 */
public interface MaterialConsumptionReportHeaderRepository extends JpaRepository<MaterialConsumptionReportHeader, Long> {
    /**
     * 通过日期判断是否存在
     *
     * @param date
     * @return
     */
    MaterialConsumptionReportHeader findFirstByDate(Date date);

    /**
     * 通过日期删除
     *
     * @param date
     */
    void deleteByDate(Date date);

    /**
     * 通过日期查询-分页
     *
     * @param startDate
     * @param endDate
     * @return
     */
    Page<MaterialConsumptionReportHeader> findByDateBetween(Date startDate, Date endDate, Pageable pageable);
}
