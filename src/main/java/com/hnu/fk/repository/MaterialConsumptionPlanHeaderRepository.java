package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialConsumptionPlanHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 11:29 2018/8/22
 * @Modified By:
 */
public interface MaterialConsumptionPlanHeaderRepository extends JpaRepository<MaterialConsumptionPlanHeader, Long> {
    /**
     * 通过日期查询
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<MaterialConsumptionPlanHeader> findByDateBetween(Date startDate, Date endDate);

    /**
     * 通过日期判断是否存在
     *
     * @param date
     * @return
     */
    MaterialConsumptionPlanHeader findFirstByDate(Date date);
}
