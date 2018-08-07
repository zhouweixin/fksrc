package com.hnu.fk.service;

import com.hnu.fk.domain.*;
import com.hnu.fk.repository.OperationRepository;
import com.hnu.fk.repository.RoleSecondLevelMenuOperationRepository;
import com.hnu.fk.repository.SecondLevelMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:45 2018/8/7
 * @Modified By:
 */
@Service
public class RoleSecondMenuOperationService {
    @Autowired
    private RoleSecondLevelMenuOperationRepository roleSecondLevelMenuOperationRepository;

    @Autowired
    private SecondLevelMenuRepository secondLevelMenuRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 通过用户主键查询所有菜单
     *
     * @param userId
     * @return
     */
    public List<Navigation> findNavigationsByRoleIds(Integer userId){
        // 查询用户的角色
        Set<Integer> roleIds = userRoleService.findRoleIdsByUserId(userId);

        // 根据角色查询所有的权限信息: role-menu-operation
        List<RoleSecondLevelMenuOperation> roleSecondLevelMenuOperations = roleSecondLevelMenuOperationRepository.findByRoleIdIn(roleIds);

        // 取出menu和operation的id, 并去重
        Set<Integer> secondLevelMenuIds = new HashSet<>();
        Set<Integer> operationIds = new HashSet<>();
        for(RoleSecondLevelMenuOperation roleSecondLevelMenuOperation : roleSecondLevelMenuOperations){
            secondLevelMenuIds.add(roleSecondLevelMenuOperation.getSecondLevelMenuId());
            operationIds.add(roleSecondLevelMenuOperation.getOperationId());
        }

        // 查询出对象
        List<SecondLevelMenu> secondLevelMenus = secondLevelMenuRepository.findAllById(secondLevelMenuIds);
        List<Operation> operations = operationRepository.findAllById(operationIds);

        // 建立主键与对象映射关系
        Map<Integer, SecondLevelMenu> secondLevelMenuMap = new HashMap<>();
        for(SecondLevelMenu secondLevelMenu : secondLevelMenus){
            secondLevelMenuMap.put(secondLevelMenu.getId(), secondLevelMenu);
        }

        // 建立主键与对象映射关系
        Map<Integer, Operation> operationMap = new HashMap<>();
        for(Operation operation : operations){
            operationMap.put(operation.getId(), operation);
        }

        // 把操作添加到二级菜单下
        for(RoleSecondLevelMenuOperation roleSecondLevelMenuOperation : roleSecondLevelMenuOperations){
            Integer secondLevelMenuId = roleSecondLevelMenuOperation.getSecondLevelMenuId();
            Integer operationId = roleSecondLevelMenuOperation.getOperationId();
            if(secondLevelMenuMap.containsKey(secondLevelMenuId) && operationMap.containsKey(operationId)){
                secondLevelMenuMap.get(secondLevelMenuId).getOperations().add(operationMap.get(operationId));
            }
        }

        // 把二级菜单添加到一级菜单下
        Map<Integer, FirstLevelMenu> firstLevelMenuMap = new HashMap<>();
        for(SecondLevelMenu secondLevelMenu : secondLevelMenus){
            FirstLevelMenu firstLevelMenu = secondLevelMenu.getFirstLevelMenu();
            if(firstLevelMenu != null && firstLevelMenu.getId() != null){
                if(firstLevelMenuMap.containsKey(firstLevelMenu.getId())){
                    firstLevelMenuMap.get(firstLevelMenu.getId()).getSecondLevelMenus().add(secondLevelMenu);
                } else {
                    firstLevelMenu.getSecondLevelMenus().add(secondLevelMenu);
                    firstLevelMenuMap.put(firstLevelMenu.getId(), firstLevelMenu);
                }
            }

            // 置空父菜单
            secondLevelMenu.setFirstLevelMenu(null);
            secondLevelMenu.setNavigation(null);
        }

        // 把一级菜单添加到导航下
        Map<Integer, Navigation> navigationHashMap = new HashMap<>();
        for(FirstLevelMenu firstLevelMenu : firstLevelMenuMap.values()){
            Navigation navigation = firstLevelMenu.getNavigation();
            if(navigation != null && navigation.getId() != null){
                if(navigationHashMap.containsKey(navigation.getId())){
                    navigationHashMap.get(navigation.getId()).getFirstLevelMenus().add(firstLevelMenu);
                } else {
                    navigation.getFirstLevelMenus().add(firstLevelMenu);
                    navigationHashMap.put(navigation.getId(), navigation);
                }
            }

            // 置空父菜单
            firstLevelMenu.setNavigation(null);
        }

        // TODO 递增排序

        List<Navigation> navigations = new ArrayList<>(navigationHashMap.values());
        return navigations;
    }
}
