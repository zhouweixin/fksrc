package com.hnu.fk.repository;

import com.hnu.fk.domain.UserRole;
import com.hnu.fk.domain.UserRoleMultiKeys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:36 2018/8/7
 * @Modified By:
 */
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleMultiKeys> {
    /**
     * 通过用户编码查询用户角色
     *
     * @param userId
     * @return
     */
    public List<UserRole> findByUserId(Integer userId);

    /**
     * 通过用户编码批量查询
     *
     * @param userIds
     * @return
     */
    public List<UserRole> findByUserIdIn(Collection<Integer> userIds);

    /**
     * 删除用户所有的角色
     *
     * @param userIds
     */
    public void deleteByUserIdIn(Collection<Integer> userIds);
}
