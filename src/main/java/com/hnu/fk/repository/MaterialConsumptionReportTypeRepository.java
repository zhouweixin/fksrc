package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialConsumptionReportTypeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 10:05 2018/8/23
 * @Modified By:
 */
public interface MaterialConsumptionReportTypeRepository extends JpaRepository<MaterialConsumptionReportTypeDetail, Long> {
}
