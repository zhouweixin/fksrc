package com.hnu.fk.service;

import com.hnu.fk.domain.*;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.NavigationRepository;
import com.hnu.fk.repository.OperationRepository;
import com.hnu.fk.repository.SecondLevelMenuOperationRepository;
import com.hnu.fk.repository.SecondLevelMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Permission;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@Service
public class NavigationService {
    @Autowired
    private NavigationRepository navigationRepository;
    @Autowired
    private SecondLevelMenuRepository secondLevelMenuRepository;
    @Autowired
    private SecondLevelMenuOperationRepository secondLevelMenuOperationRepository;
    @Autowired
    private OperationRepository operationRepository;

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

        return navigationRepository.save(navigation);
    }

    /**
     * 更新
     *
     * @param navigation
     * @return
     */
    public Navigation update(Navigation navigation) {

        // 验证是否存在
        if (navigation == null || navigation.getId() == null || navigationRepository.findById(navigation.getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        return navigationRepository.save(navigation);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {

        // 验证是否存在
        if (navigationRepository.findById(id).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        navigationRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
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
     * 查询所有子菜单及其允许操作
     */
    public List<Navigation> findAllNavigationOperations(){
        //得到所有允许操作
        List<SecondLevelMenuOperation> secondLevelMenuOperations = secondLevelMenuOperationRepository.findAll();
        //得到对象
        List<SecondLevelMenu> secondLevelMenus = secondLevelMenuRepository.findAll();
        List<Operation> operations = operationRepository.findAll();

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

        // TODO 递增排序

        List<Navigation> navigations = new ArrayList<>(navigationHashMap.values());
        return navigations;
    }


}
