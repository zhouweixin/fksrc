package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialConsumptionHeader;
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
}
