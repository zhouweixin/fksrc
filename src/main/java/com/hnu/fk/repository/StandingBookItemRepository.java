package com.hnu.fk.repository;

import com.hnu.fk.domain.StandingBookItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StandingBookItemRepository extends JpaRepository<StandingBookItem,Integer> {
    /**
     * 根据ids批量删除
     * @param ids
     */
    void deleteByIdIn(Collection<Integer> ids);
    /**
     * 根据台账项目类别和项目名称模糊查询-分页
     */
    Page<StandingBookItem> findByItemNameLikeAndStandingBookTypeId(String name, Integer id, Pageable pageable);
}
