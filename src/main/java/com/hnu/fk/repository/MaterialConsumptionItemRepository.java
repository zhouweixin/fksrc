package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialConsumptionItem;
import com.hnu.fk.domain.MaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 17:40 2018/8/20
 * @Modified By:
 */
public interface MaterialConsumptionItemRepository extends JpaRepository<MaterialConsumptionItem, Integer> {
    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteByIdIn(List<Integer> ids);

    /**
     * 通过名称模糊查询
     *
     * @param name
     * @param pageable
     * @return
     */
    Page<MaterialConsumptionItem> findByNameLike(String name, Pageable pageable);

    /**
     * 通过物料类型查询-分页
     *
     * @param materialType
     * @param pageable
     * @return
     */
    Page<MaterialConsumptionItem> findByMaterialType(MaterialType materialType, Pageable pageable);

    /**
     * 通过物料类型和名称模糊查询-分页
     *
     * @param materialType
     * @param pageable
     * @return
     */
    Page<MaterialConsumptionItem> findByMaterialTypeAndNameLike(MaterialType materialType, String name, Pageable pageable);

    /**
     * 通过物料类型查询
     *
     * @param materialType
     * @return
     */
    MaterialConsumptionItem findFirstByMaterialType(MaterialType materialType);
}
