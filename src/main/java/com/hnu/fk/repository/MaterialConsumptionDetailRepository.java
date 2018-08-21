package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialConsumptionDetail;
import com.hnu.fk.domain.MaterialConsumptionHeader;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 14:02 2018/8/21
 * @Modified By:
 */
public interface MaterialConsumptionDetailRepository extends JpaRepository<MaterialConsumptionDetail, Long> {
    /**
     * 通过表头删除数据
     *
     * @param header
     */
    void deleteByHeader(MaterialConsumptionHeader header);
}
