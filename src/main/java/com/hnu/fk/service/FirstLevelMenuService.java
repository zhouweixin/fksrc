package com.hnu.fk.service;

import com.hnu.fk.domain.FirstLevelMenu;
import com.hnu.fk.domain.Navigation;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.FirstLevelMenuRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.BeanUtils;
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
public class FirstLevelMenuService {
    public static final String NAME = "一级菜单";

    @Autowired
    private FirstLevelMenuRepository firstLevelMenuRepository;

    /**
     * 新增
     *
     * @param firstLevelMenu
     * @return
     */
    public FirstLevelMenu save(FirstLevelMenu firstLevelMenu) {

        // 验证是否存在
        if (firstLevelMenu == null || (firstLevelMenu.getId() != null && firstLevelMenuRepository.findById(firstLevelMenu.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        FirstLevelMenu save = firstLevelMenuRepository.save(firstLevelMenu);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新
     *
     * @param firstLevelMenu
     * @return
     */
    public FirstLevelMenu update(FirstLevelMenu firstLevelMenu) {

        // 验证是否存在
        Optional<FirstLevelMenu> optional = null;
        if (firstLevelMenu == null || firstLevelMenu.getId() == null || (optional=firstLevelMenuRepository.findById(firstLevelMenu.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        FirstLevelMenu oldFirstLevelMenu = optional.get();
        FirstLevelMenu newFirstLevelMenu = firstLevelMenuRepository.save(firstLevelMenu);
        ActionLogUtil.log(NAME, oldFirstLevelMenu, newFirstLevelMenu);

        return newFirstLevelMenu;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {

        // 验证是否存在
        Optional<FirstLevelMenu> optional = null;
        if ((optional=firstLevelMenuRepository.findById(id)).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }

        ActionLogUtil.log(NAME, 1, optional.get());
        firstLevelMenuRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
        ActionLogUtil.log(NAME, firstLevelMenuRepository.findAllById(Arrays.asList(ids)));
        firstLevelMenuRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public FirstLevelMenu findOne(Integer id) {
        Optional<FirstLevelMenu> optional = firstLevelMenuRepository.findById(id);
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
    public List<FirstLevelMenu> findAll() {
        return firstLevelMenuRepository.findAll();
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
    public Page<FirstLevelMenu> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            FirstLevelMenu.class.getDeclaredField(sortFieldName);
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
        return firstLevelMenuRepository.findAll(pageable);
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
    public Page<FirstLevelMenu> findByNameLikeByPage(String name, Integer page, Integer size, String sortFieldName,
                                             Integer asc) {

        // 判断排序字段名是否存在
        try {
            FirstLevelMenu.class.getDeclaredField(sortFieldName);
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
        return firstLevelMenuRepository.findByNameLike("%" + name + "%", pageable);
    }

    /**
     * 上下移动菜单
     *
     * @param id1
     * @param id2
     */
    public void shift(Integer id1, Integer id2) {
        Optional<FirstLevelMenu> optional1 = firstLevelMenuRepository.findById(id1);
        Optional<FirstLevelMenu> optional2 = firstLevelMenuRepository.findById(id2);

        if(optional1.isPresent() == false || optional1.isPresent() == false){
            throw new FkExceptions(EnumExceptions.MENU_SHIFT_FAILED_NOT_EXISTS);
        }

        // 复制旧数据
        FirstLevelMenu oldFirstLevelMenu1 = new FirstLevelMenu();
        FirstLevelMenu oldFirstLevelMenu2 = new FirstLevelMenu();
        BeanUtils.copyProperties(optional1.get(), oldFirstLevelMenu1);
        BeanUtils.copyProperties(optional2.get(), oldFirstLevelMenu2);

        // 交换序号
        int temp = optional1.get().getRank();
        optional1.get().setRank(optional2.get().getRank());
        optional2.get().setRank(temp);

        // 更新数据
        FirstLevelMenu newFirstLevelMenu1 = firstLevelMenuRepository.save(optional1.get());
        FirstLevelMenu newFirstLevelMenu2 = firstLevelMenuRepository.save(optional2.get());

        // 保存日志
        ActionLogUtil.log(NAME, oldFirstLevelMenu1, newFirstLevelMenu1);
        ActionLogUtil.log(NAME, oldFirstLevelMenu2, newFirstLevelMenu2);
    }
}
