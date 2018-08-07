package com.hnu.fk.service;

import com.hnu.fk.domain.UserRole;
import com.hnu.fk.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:42 2018/8/7
 * @Modified By:
 */
@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * 通过用户id查询所有角色
     *
     * @param userId
     * @return
     */
    public Set<Integer> findRoleIdsByUserId(Integer userId){
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        Set<Integer> roleIds = new HashSet<>();
        for(UserRole userRole : userRoles){
            roleIds.add(userRole.getRoleId());
        }

        return roleIds;
    }
}
