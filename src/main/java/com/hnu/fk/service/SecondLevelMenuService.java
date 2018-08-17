package com.hnu.fk.service;

import com.hnu.fk.domain.FirstLevelMenu;
import com.hnu.fk.domain.Operation;
import com.hnu.fk.domain.SecondLevelMenu;
import com.hnu.fk.domain.SecondLevelMenuOperation;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.OperationRepository;
import com.hnu.fk.repository.SecondLevelMenuOperationRepository;
import com.hnu.fk.repository.SecondLevelMenuRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.BeanUtils;
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
public class SecondLevelMenuService {

    public static final String NAME = "二级菜单";

    @Autowired
    private SecondLevelMenuRepository secondLevelRepositoryRepository;

    @Autowired
    private SecondLevelMenuOperationRepository secondLevelMenuOperationRepository;

    @Autowired
    private OperationRepository operationRepository;

    /**
     * 新增
     *
     * @param secondLevelRepository
     * @return
     */
    public SecondLevelMenu save(SecondLevelMenu secondLevelRepository) {

        // 验证是否存在
        if (secondLevelRepository == null || (secondLevelRepository.getId() != null && secondLevelRepositoryRepository.findById(secondLevelRepository.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        SecondLevelMenu save = secondLevelRepositoryRepository.save(secondLevelRepository);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新
     *
     * @param secondLevelRepository
     * @return
     */
    public SecondLevelMenu update(SecondLevelMenu secondLevelRepository) {

        // 验证是否存在
        Optional<SecondLevelMenu> optional = null;
        if (secondLevelRepository == null || secondLevelRepository.getId() == null || (optional=secondLevelRepositoryRepository.findById(secondLevelRepository.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        SecondLevelMenu oldSecondLevelMenu = optional.get();
        SecondLevelMenu newSecondLevelMenu = secondLevelRepositoryRepository.save(secondLevelRepository);
        ActionLogUtil.log(NAME, oldSecondLevelMenu, newSecondLevelMenu);
        return newSecondLevelMenu;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {

        // 验证是否存在
        Optional<SecondLevelMenu> optional = null;
        if ((optional=secondLevelRepositoryRepository.findById(id)).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }

        ActionLogUtil.log(NAME, 1, optional.get());
        secondLevelRepositoryRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
        ActionLogUtil.log(NAME, 1, secondLevelRepositoryRepository.findAllById(Arrays.asList(ids)));
        secondLevelRepositoryRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public SecondLevelMenu findOne(Integer id) {
        Optional<SecondLevelMenu> optional = secondLevelRepositoryRepository.findById(id);
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
    public List<SecondLevelMenu> findAll() {
        return secondLevelRepositoryRepository.findAll();
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
    public Page<SecondLevelMenu> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            SecondLevelMenu.class.getDeclaredField(sortFieldName);
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
        return secondLevelRepositoryRepository.findAll(pageable);
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
    public Page<SecondLevelMenu> findByNameLikeByPage(String name, Integer page, Integer size, String sortFieldName,
                                             Integer asc) {

        // 判断排序字段名是否存在
        try {
            SecondLevelMenu.class.getDeclaredField(sortFieldName);
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
        return secondLevelRepositoryRepository.findByNameLike("%" + name + "%", pageable);
    }

    /**
     * 菜单上下移动
     *
     * @param id1
     * @param id2
     */
    public void shift(Integer id1, Integer id2) {
        Optional<SecondLevelMenu> optional1 = secondLevelRepositoryRepository.findById(id1);
        Optional<SecondLevelMenu> optional2 = secondLevelRepositoryRepository.findById(id2);

        if(optional1.isPresent() == false || optional1.isPresent() == false){
            throw new FkExceptions(EnumExceptions.MENU_SHIFT_FAILED_NOT_EXISTS);
        }

        // 复制旧数据
        SecondLevelMenu oldSecondLevelMenu1 = new SecondLevelMenu();
        SecondLevelMenu oldSecondLevelMenu2 = new SecondLevelMenu();
        BeanUtils.copyProperties(optional1.get(), oldSecondLevelMenu1);
        BeanUtils.copyProperties(optional2.get(), oldSecondLevelMenu2);

        // 交换序号
        int temp = optional1.get().getRank();
        optional1.get().setRank(optional2.get().getRank());
        optional2.get().setRank(temp);

        // 更新数据
        SecondLevelMenu newSecondLevelMenu1 = secondLevelRepositoryRepository.save(optional1.get());
        SecondLevelMenu newSecondLevelMenu2 = secondLevelRepositoryRepository.save(optional2.get());

        // 保存日志
        ActionLogUtil.log(NAME, oldSecondLevelMenu1, newSecondLevelMenu1);
        ActionLogUtil.log(NAME, oldSecondLevelMenu2, newSecondLevelMenu2);
    }

    /**
     * 通过菜单主键查询所有操作
     * @param id
     * @return
     */
    public List<Operation> getOperationsById(Integer id) {
        List<SecondLevelMenuOperation> menuOperations = secondLevelMenuOperationRepository.findBySecondLevelMenuId(id);
        Set<Integer> operationIds = new HashSet<>();
        for(SecondLevelMenuOperation menuOperation : menuOperations){
            operationIds.add(menuOperation.getOperationId());
        }

        return operationRepository.findAllById(operationIds);
    }

    /**
     * 给二级菜单分配操作
     *
     * @param menuId
     * @param operationIds
     */
    @Transactional
    public void assignOperations(Integer menuId, Integer[] operationIds) {
        // 删除旧的分配
        secondLevelMenuOperationRepository.deleteBySecondLevelMenuId(menuId);

        List<SecondLevelMenuOperation> menuOperations = new ArrayList<>();
        for(Integer operationId : operationIds){
            menuOperations.add(new SecondLevelMenuOperation(menuId, operationId));
        }

        secondLevelMenuOperationRepository.saveAll(menuOperations);
    }
}
