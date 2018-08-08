package com.hnu.fk.service;

import com.hnu.fk.domain.SecondLevelMenu;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.SecondLevelMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@Service
public class SecondLevelMenuService {
    @Autowired
    private SecondLevelMenuRepository secondLevelRepositoryRepository;

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

        return secondLevelRepositoryRepository.save(secondLevelRepository);
    }

    /**
     * 更新
     *
     * @param secondLevelRepository
     * @return
     */
    public SecondLevelMenu update(SecondLevelMenu secondLevelRepository) {

        // 验证是否存在
        if (secondLevelRepository == null || secondLevelRepository.getId() == null || secondLevelRepositoryRepository.findById(secondLevelRepository.getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        return secondLevelRepositoryRepository.save(secondLevelRepository);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {

        // 验证是否存在
        if (secondLevelRepositoryRepository.findById(id).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        secondLevelRepositoryRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
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

        int temp = optional1.get().getRank();
        optional1.get().setRank(optional2.get().getRank());
        optional2.get().setRank(temp);
        secondLevelRepositoryRepository.save(optional1.get());
        secondLevelRepositoryRepository.save(optional2.get());
    }
}
