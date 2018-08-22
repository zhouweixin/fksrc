package com.hnu.fk.repository;

import com.hnu.fk.domain.StandingBookType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StandingBookTypeRepository extends JpaRepository<StandingBookType,Integer> {
    /**
     * 批量删除
     * @param ids
     */
    void deleteByIdIn(Collection<Integer> ids);

    /**
     * 根据所属工段和名称模糊查询
     */
    Page<StandingBookType> findByItemTypeNameLikeAndSectionInfoId(String itemTypeName, Integer sectionId, Pageable pageable);
}
