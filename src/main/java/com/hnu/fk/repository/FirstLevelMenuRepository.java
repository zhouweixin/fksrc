package com.hnu.fk.repository;

import com.hnu.fk.domain.FirstLevelMenu;
import com.hnu.fk.domain.Navigation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:23 2018/8/6
 * @Modified By:
 */
public interface FirstLevelMenuRepository extends JpaRepository<FirstLevelMenu, Integer> {
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
    public Page<FirstLevelMenu> findByNameLike(String name, Pageable pageable);

    /**
     * 查询最大排序
     *
     * @return
     */
    @Query(value = "select max(n.rank) from FirstLevelMenu n")
    public Integer findMaxRank();

    /**
     * 通过导航查询所有一级菜单
     *
     * @param navigation
     * @return
     */
    public List<FirstLevelMenu> findByNavigation(Navigation navigation);
}
