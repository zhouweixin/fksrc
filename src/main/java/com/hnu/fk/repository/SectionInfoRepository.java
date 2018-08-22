package com.hnu.fk.repository;

import com.hnu.fk.domain.SectionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface SectionInfoRepository extends JpaRepository<SectionInfo,Integer> {
    /**
     * 批量删除
     */
    void deleteByIdIn(Collection<Integer> ids);

    /**
     * 通过名称模糊查询-分页
     */
    Page<SectionInfo> findBySectionNameLike(String name, Pageable pageable);
}
