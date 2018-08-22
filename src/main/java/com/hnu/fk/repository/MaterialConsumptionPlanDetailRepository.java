package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialConsumptionPlanDetail;
import com.hnu.fk.domain.MaterialConsumptionPlanHeader;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 12:24 2018/8/22
 * @Modified By:
 */
public interface MaterialConsumptionPlanDetailRepository extends JpaRepository<MaterialConsumptionPlanDetail, Long> {
    /**
     * 通过表头删除数据
     *
     * @param materialConsumptionPlanHeader
     */
    void deleteByHeader(MaterialConsumptionPlanHeader materialConsumptionPlanHeader);
}
