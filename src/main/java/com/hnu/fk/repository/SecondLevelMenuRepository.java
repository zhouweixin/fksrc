package com.hnu.fk.repository;

import com.hnu.fk.domain.Department;
import com.hnu.fk.domain.FirstLevelMenu;
import com.hnu.fk.domain.SecondLevelMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:23 2018/8/6
 * @Modified By:
 */
public interface SecondLevelMenuRepository extends JpaRepository<SecondLevelMenu, Integer> {
    /**
     * 通过主键批量删除
     *
     * @param ids
     */
    public void deleteByIdIn(Collection<Integer> ids);

    /**
     * 通过名称模糊查询-分页
     *
     * @param name
     * @param pageable
     * @return
     */
    public Page<SecondLevelMenu> findByNameLike(String name, Pageable pageable);
}
