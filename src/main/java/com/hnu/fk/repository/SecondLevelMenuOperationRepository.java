package com.hnu.fk.repository;

import com.hnu.fk.domain.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:23 2018/8/6
 * @Modified By:
 */
public interface SecondLevelMenuOperationRepository extends JpaRepository<SecondLevelMenuOperation, Integer> {
    /**
     * 通过二级菜单id删除
     */
    void deleteBySecondLevelMenuId(Integer secondLevelMenuId);
}
