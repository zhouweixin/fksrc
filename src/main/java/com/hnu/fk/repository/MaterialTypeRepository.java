package com.hnu.fk.repository;

import com.hnu.fk.domain.MaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:35 2018/8/20
 * @Modified By:
 */
public interface MaterialTypeRepository extends JpaRepository<MaterialType, Integer> {
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
    Page<MaterialType> findByNameLike(String name, Pageable pageable);
}
