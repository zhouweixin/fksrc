package com.hnu.fk.service;

import com.hnu.fk.domain.*;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.*;
import org.springframework.beans.BeanUtils;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
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
public class NavigationService {
    public static final String NAME = "导航菜单";
    @Autowired
    private NavigationRepository navigationRepository;
    @Autowired
    private SecondLevelMenuRepository secondLevelMenuRepository;
    @Autowired
    private SecondLevelMenuOperationRepository secondLevelMenuOperationRepository;
    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleSecondLevelMenuOperationRepository roleSecondLevelMenuOperationRepository;

    @Autowired
    private FirstLevelMenuRepository firstLevelMenuRepository;

    /**
     * 新增
     *
     * @param navigation
     * @return
     */
    public Navigation save(Navigation navigation) {

        // 验证是否存在
        if (navigation == null || (navigation.getId() != null && navigationRepository.findById(navigation.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        Integer maxRank = navigationRepository.findMaxRank();
        int rank = maxRank == null ? 1 : maxRank + 1;
        navigation.setRank(rank);

        Navigation save = navigationRepository.save(navigation);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新
     *
     * @param navigation
     * @return
     */
    public Navigation update(Navigation navigation) {

        // 验证是否存在
        Optional<Navigation> optional = null;
        if (navigation == null || navigation.getId() == null || (optional=navigationRepository.findById(navigation.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        Navigation oldNavigation = optional.get();
        Navigation newNavigation = navigationRepository.save(navigation);
        ActionLogUtil.log(NAME, oldNavigation, newNavigation);

        return newNavigation;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {

        // 验证是否存在
        Optional<Navigation> optional = null;
        if ((optional=navigationRepository.findById(id)).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }

        ActionLogUtil.log(NAME, 1, optional.get());
        navigationRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
        ActionLogUtil.log(NAME, 1, navigationRepository.findAllById(Arrays.asList(ids)));
        navigationRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public Navigation findOne(Integer id) {
        Optional<Navigation> optional = navigationRepository.findById(id);
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
    public List<Navigation> findAll() {
        return navigationRepository.findAll();
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
    public Page<Navigation> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            Navigation.class.getDeclaredField(sortFieldName);
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
        return navigationRepository.findAll(pageable);
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
    public Page<Navigation> findByNameLikeByPage(String name, Integer page, Integer size, String sortFieldName,
                                                 Integer asc) {

        // 判断排序字段名是否存在
        try {
            Navigation.class.getDeclaredField(sortFieldName);
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
        return navigationRepository.findByNameLike("%" + name + "%", pageable);
    }

    /**
     * 查询所有菜单及其可以分配操作
     * @return
     */
    public List<Navigation> findAllNavigationOperations(){
        //得到所有允许操作
        List<SecondLevelMenuOperation> secondLevelMenuOperations = secondLevelMenuOperationRepository.findAll();
        //得到对象
        List<SecondLevelMenu> secondLevelMenus = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();

        // 循环copy对象
        for(SecondLevelMenu secondLevelMenu : secondLevelMenuRepository.findAll()){

            // 1、copy 二级菜单
            // 创建的新对象
            SecondLevelMenu menu = new SecondLevelMenu();

            // 执行copy
            BeanUtils.copyProperties(secondLevelMenu, menu);

            // 添加到新的list里
            secondLevelMenus.add(menu);

            // 1、copy 一级菜单
            FirstLevelMenu firstLevelMenu = new FirstLevelMenu();
            BeanUtils.copyProperties(menu.getFirstLevelMenu(), firstLevelMenu);
            menu.setFirstLevelMenu(firstLevelMenu);
        }

        for(Operation operation : operationRepository.findAll()){
            Operation o = new Operation();
            BeanUtils.copyProperties(operation, o);
            operations.add(o);
        }

        // 建立主键与对象映射关系 SecondLevelMenu
        Map<Integer, SecondLevelMenu> secondLevelMenuMap = new HashMap<>();
        for(SecondLevelMenu secondLevelMenu : secondLevelMenus){
            secondLevelMenuMap.put(secondLevelMenu.getId(), secondLevelMenu);
        }

        // 建立主键与对象映射关系 Operation
        Map<Integer, Operation> operationMap = new HashMap<>();
        for(Operation operation : operations){
            operationMap.put(operation.getId(), operation);
        }
        /**
         * 把允许操作添加到二级菜单下
         */
        //根据二级菜单操作分配表来分配操作
        for(SecondLevelMenuOperation secondLevelMenuOperation:secondLevelMenuOperations){
            Integer secondLevelMenuId = secondLevelMenuOperation.getSecondLevelMenuId();
            Integer operationId = secondLevelMenuOperation.getOperationId();
            if(secondLevelMenuMap.containsKey(secondLevelMenuId)&&operationMap.containsKey(operationId)){
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

        List<Navigation> navigations = new ArrayList<>(navigationHashMap.values());
        return navigations;
    }

    /**
     * 更新菜单可分配操作
     * @param secondMenuId
     * @param operationIds
     * @return
     */
    @Transactional
    public List<SecondLevelMenuOperation> updateSecondLevelMenuOperatons(Integer secondMenuId,Integer[] operationIds){
        //验证是否存在
        if (secondLevelMenuRepository.findById(secondMenuId).isPresent()==false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST); }
        for(Integer id : operationIds){
            if(operationRepository.findById(id).isPresent()==false){
                throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
            }
        }
        secondLevelMenuOperationRepository.deleteBySecondLevelMenuId(secondMenuId);
        List<SecondLevelMenuOperation> secondLevelMenuOperations = new ArrayList<>();
        for(Integer operationId :operationIds){
            SecondLevelMenuOperation secondLevelMenuOperation = new SecondLevelMenuOperation();
            secondLevelMenuOperation.setSecondLevelMenuId(secondMenuId);
            secondLevelMenuOperation.setOperationId(operationId);
            secondLevelMenuOperations.add(secondLevelMenuOperation);
        }
        ActionLogUtil.log("二级菜单操作关系",0,secondLevelMenuOperations);
        return secondLevelMenuOperationRepository.saveAll(secondLevelMenuOperations);
    }

    /**
     * 通过用户主键查询所有菜单
     *
     * @param userId
     * @return
     */
    public List<Navigation> findAllByUserId(Integer userId) {
        // 查询用户的角色
        Set<Integer> roleIds = userRoleService.findRoleIdsByUserId(userId);

        return findAllByRoleIds(roleIds);
    }

    /**
     * 通过角色查询所有菜单
     *
     * @param roleIds
     * @return
     */
    public List<Navigation> findAllByRoleIds(Set<Integer> roleIds){
        // 根据角色查询所有的权限信息: role-menu-operation
        List<RoleSecondLevelMenuOperation> roleSecondLevelMenuOperations = roleSecondLevelMenuOperationRepository.findByRoleIdIn(roleIds);

        // 取出menu和operation的id, 并去重
        Set<Integer> secondLevelMenuIds = new HashSet<>();
        Set<Integer> operationIds = new HashSet<>();
        for (RoleSecondLevelMenuOperation roleSecondLevelMenuOperation : roleSecondLevelMenuOperations) {
            secondLevelMenuIds.add(roleSecondLevelMenuOperation.getSecondLevelMenuId());
            operationIds.add(roleSecondLevelMenuOperation.getOperationId());
        }

        // 查询出对象
        List<SecondLevelMenu> secondLevelMenus = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();

        // 循环copy对象
        for(SecondLevelMenu secondLevelMenu : secondLevelMenuRepository.findAllById(secondLevelMenuIds)){

            // 1、copy 二级菜单
            // 创建的新对象
            SecondLevelMenu menu = new SecondLevelMenu();

            // 执行copy
            BeanUtils.copyProperties(secondLevelMenu, menu);

            // 添加到新的list里
            secondLevelMenus.add(menu);

            // 1、copy 一级菜单
            FirstLevelMenu firstLevelMenu = new FirstLevelMenu();
            BeanUtils.copyProperties(menu.getFirstLevelMenu(), firstLevelMenu);
            menu.setFirstLevelMenu(firstLevelMenu);
        }

        for(Operation operation : operationRepository.findAllById(operationIds)){
            Operation o = new Operation();
            BeanUtils.copyProperties(operation, o);
            operations.add(o);
        }

        // 建立主键与对象映射关系
        Map<Integer, SecondLevelMenu> secondLevelMenuMap = new HashMap<>();
        for (SecondLevelMenu secondLevelMenu : secondLevelMenus) {
            secondLevelMenuMap.put(secondLevelMenu.getId(), secondLevelMenu);
        }

        // 建立主键与对象映射关系
        Map<Integer, Operation> operationMap = new HashMap<>();
        for (Operation operation : operations) {
            operationMap.put(operation.getId(), operation);
        }

        // 把操作添加到二级菜单下
        for (RoleSecondLevelMenuOperation roleSecondLevelMenuOperation : roleSecondLevelMenuOperations) {
            Integer secondLevelMenuId = roleSecondLevelMenuOperation.getSecondLevelMenuId();
            Integer operationId = roleSecondLevelMenuOperation.getOperationId();
            if (secondLevelMenuMap.containsKey(secondLevelMenuId) && operationMap.containsKey(operationId)) {
                secondLevelMenuMap.get(secondLevelMenuId).getOperations().add(operationMap.get(operationId));
            }
        }

        // 把二级菜单添加到一级菜单下
        Map<Integer, FirstLevelMenu> firstLevelMenuMap = new HashMap<>();
        for (SecondLevelMenu secondLevelMenu : secondLevelMenus) {
            FirstLevelMenu firstLevelMenu = secondLevelMenu.getFirstLevelMenu();
            if (firstLevelMenu != null && firstLevelMenu.getId() != null) {
                if (firstLevelMenuMap.containsKey(firstLevelMenu.getId())) {
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
        for (FirstLevelMenu firstLevelMenu : firstLevelMenuMap.values()) {
            Navigation navigation = firstLevelMenu.getNavigation();
            if (navigation != null && navigation.getId() != null) {
                if (navigationHashMap.containsKey(navigation.getId())) {
                    navigationHashMap.get(navigation.getId()).getFirstLevelMenus().add(firstLevelMenu);
                } else {
                    navigation.getFirstLevelMenus().add(firstLevelMenu);
                    navigationHashMap.put(navigation.getId(), navigation);
                }
            }

            // 置空父菜单
            firstLevelMenu.setNavigation(null);
        }

        List<Navigation> navigations = new ArrayList<>(navigationHashMap.values());
        return navigations;
    }

    /**
     * 通过导航主键查询所有一级菜单
     *
     * @param id
     * @return
     */
    public List<FirstLevelMenu> getFirstLevelMenusById(Integer id) {
        Navigation navigation = new Navigation();
        navigation.setId(id);
        return firstLevelMenuRepository.findByNavigation(navigation);
    }

    /**
     * 更新名称
     *
     * @param id
     * @param name
     */
    public void updateNameById(Integer id, String name) {
        // 验证是否存在
        Optional<Navigation> optional = navigationRepository.findById(id);
        if (optional.isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        Navigation oldNavigation = optional.get();
        Navigation newNavigation = new Navigation();

        BeanUtils.copyProperties(oldNavigation, newNavigation);
        newNavigation.setName(name);
        navigationRepository.save(newNavigation);
        ActionLogUtil.log(NAME, oldNavigation, newNavigation);
    }

    /**
     * 上下移动菜单
     *
     * @param id1
     * @param id2
     */
    public void shift(Integer id1, Integer id2) {
        Optional<Navigation> optional1 = navigationRepository.findById(id1);
        Optional<Navigation> optional2 = navigationRepository.findById(id2);

        if(optional1.isPresent() == false || optional1.isPresent() == false){
            throw new FkExceptions(EnumExceptions.MENU_SHIFT_FAILED_NOT_EXISTS);
        }

        // 复制旧数据
        Navigation oldNavigation1 = new Navigation();
        Navigation oldNavigation2 = new Navigation();
        BeanUtils.copyProperties(optional1.get(), oldNavigation1);
        BeanUtils.copyProperties(optional2.get(), oldNavigation2);

        // 交换序号
        int temp = optional1.get().getRank();
        optional1.get().setRank(optional2.get().getRank());
        optional2.get().setRank(temp);

        // 更新数据
        Navigation newNavigation1 = navigationRepository.save(optional1.get());
        Navigation newNavigation2 = navigationRepository.save(optional2.get());

        // 保存日志
        ActionLogUtil.log(NAME, oldNavigation1, newNavigation1);
        ActionLogUtil.log(NAME, oldNavigation2, newNavigation2);
    }
}
