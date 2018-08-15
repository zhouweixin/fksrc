package com.hnu.fk.service;

import com.hnu.fk.domain.*;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.RoleRepository;
import com.hnu.fk.repository.RoleSecondLevelMenuOperationRepository;
import com.hnu.fk.repository.UserRepository;
import com.hnu.fk.repository.UserRoleRepository;
import com.hnu.fk.utils.ActionLogUtil;
import com.hnu.fk.vo.RoleAssignUsersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@Service
public class RoleService {
    public static final String NAME = "角色";
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NavigationService navigationService;

    @Autowired
    private RoleSecondLevelMenuOperationRepository roleSecondLevelMenuOperationRepository;

    /**
     * 新增
     *
     * @param role
     * @return
     */
    public Role save(Role role) {

        // 验证是否存在
        if (role == null || (role.getId() != null && roleRepository.findById(role.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        Role save = roleRepository.save(role);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新
     *
     * @param role
     * @return
     */
    public Role update(Role role) {

        // 验证是否存在
        Optional<Role> optional = null;
        if (role == null || role.getId() == null || (optional=roleRepository.findById(role.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        role.setUpdateTime(new Date());

        Role oldRole = optional.get();
        Role newRole = roleRepository.save(role);
        ActionLogUtil.log(NAME, oldRole, newRole);
        return newRole;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {
        // 验证是否存在
        Optional<Role> optional = roleRepository.findById(id);
        if (optional.isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }

        // 验证是否是系统值
        if(optional.get().getFlag() == 1){
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_SYSTEM_VALUE_NOT_ALLOW);
        }

        ActionLogUtil.log(NAME, 1, optional.get());
        roleRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
        ActionLogUtil.log(NAME, 1, roleRepository.findByIdInAndFlag(Arrays.asList(ids), 0));
        // 非系统值才可以删除
        roleRepository.deleteByIdInAndFlag(Arrays.asList(ids), 0);
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public Role findOne(Integer id) {
        Optional<Role> optional = roleRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * 查询所有-分页
     *
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<Role> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            Role.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            // 如果不存在就设置为id
            sortFieldName = "id";
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        Pageable pageable =PageRequest.of(page, size, sort);
        return roleRepository.findAll(pageable);
    }

    /**
     * 通过名称模糊分页查询
     *
     * @param name
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<Role> findByNameLikeByPage(String name, Integer page, Integer size, String sortFieldName,
                                             Integer asc) {

        // 判断排序字段名是否存在
        try {
            Role.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            // 如果不存在就设置为id
            sortFieldName = "id";
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        Pageable pageable =PageRequest.of(page, size, sort);
        return roleRepository.findByNameLike("%" + name + "%", pageable);
    }

    /**
     * 分配权限给角色
     *
     * @param permissions
     */
    @Transactional
    public void assignPermissions(Set<RoleSecondLevelMenuOperation> permissions) {
        Set<Integer> roleIds = new HashSet<>();
        for(RoleSecondLevelMenuOperation permission : permissions){
            roleIds.add(permission.getRoleId());
        }

        // 先清除角色的权限
        ActionLogUtil.log(NAME, 1, roleSecondLevelMenuOperationRepository.findByRoleIdIn(roleIds));
        roleSecondLevelMenuOperationRepository.deleteByRoleIdIn(roleIds);

        // 分配
        List<RoleSecondLevelMenuOperation> roleSecondLevelMenuOperations = roleSecondLevelMenuOperationRepository.saveAll(permissions);
        ActionLogUtil.log(NAME, 0, roleSecondLevelMenuOperations);
    }

    /**
     * 查询角色分配的用户
     *
     * @param id
     * @return
     */
    public RoleAssignUsersVO getAssignUsersById(Integer id) {
        // 查询所有的用户角色关系
        List<UserRole> userRoles = userRoleRepository.findByRoleId(id);

        // 提取所有的用户主键并去重
        Set<Integer> userIds = new HashSet<>();
        for(UserRole userRole : userRoles){
            userIds.add(userRole.getUserId());
        }

        List<User> assignUsers = userRepository.findAllById(userIds);
        List<User> allUsers = userRepository.findAll();
        return new RoleAssignUsersVO(assignUsers, allUsers);
    }

    /**
     * 分配角色给用户
     *
     * @param roleId
     * @param userIds
     */
    @Transactional
    public void assignRoleToUsers(Integer roleId, Integer[] userIds) {
        // 验证是否存在
        Optional<Role> optional = roleRepository.findById(roleId);
        if (optional.isPresent() == false) {
            throw new FkExceptions(EnumExceptions.ASSIGN_FAILED_ROLE_NOT_EXISTS);
        }

        // 清除所有角色
        userRoleRepository.deleteByRoleId(roleId);

        // 查询所有的用户
        List<User> users = userRepository.findAllById(Arrays.asList(userIds));
        Set<Integer> userIdSet = new HashSet<>();
        for(User user : users){
            userIdSet.add(user.getId());
        }

        List<UserRole> userRoles = new ArrayList<>();
        for(int userId : userIdSet){
            userRoles.add(new UserRole(userId, roleId));
        }
        userRoleRepository.saveAll(userRoles);
    }

    /**
     * 查询角色分配的权限
     * @param id
     * @return
     */
    public List<Navigation> getPermissionsById(int id) {
        HashSet<Integer> set = new HashSet<>();
        set.add(id);
        return navigationService.findAllByRoleIds(set);
    }
}
