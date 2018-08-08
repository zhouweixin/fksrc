package com.hnu.fk.repository;

import com.hnu.fk.domain.Role;
import com.hnu.fk.domain.RoleSecondLevelMenuOperation;
import com.hnu.fk.domain.RoleSecondLevelMenuOperationMultiKeys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:23 2018/8/6
 * @Modified By:
 */
public interface RoleSecondLevelMenuOperationRepository extends JpaRepository<RoleSecondLevelMenuOperation, RoleSecondLevelMenuOperationMultiKeys> {
    /**
     * 通过用户角色查询所有权限
     *
     * @param roleIds
     * @return
     */
    public List<RoleSecondLevelMenuOperation> findByRoleIdIn(Collection<Integer> roleIds);

    /**
     * 通过角色删除
     *
     * @param roleIds
     */
    public void deleteByRoleIdIn(Collection<Integer> roleIds);
}
