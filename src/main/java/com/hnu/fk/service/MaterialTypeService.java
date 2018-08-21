package com.hnu.fk.service;

import com.hnu.fk.domain.MaterialConsumptionItem;
import com.hnu.fk.domain.MaterialType;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.MaterialConsumptionItemRepository;
import com.hnu.fk.repository.MaterialTypeRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:36 2018/8/20
 * @Modified By:
 */
@Service
public class MaterialTypeService {

    public static final String NAME = "物料消耗项目";

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private MaterialConsumptionItemRepository materialConsumptionItemRepository;

    /**
     * 新增
     *
     * @param materialType
     * @return
     */
    public MaterialType save(MaterialType materialType) {

        // 验证是否存在
        if (materialType == null || (materialType.getId() != null && materialTypeRepository.findById(materialType.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        MaterialType save = materialTypeRepository.save(materialType);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新
     *
     * @param materialType
     * @return
     */
    public MaterialType update(MaterialType materialType) {

        // 验证是否存在
        Optional<MaterialType> optional = null;
        if (materialType == null || materialType.getId() == null || (optional=materialTypeRepository.findById(materialType.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        MaterialType oldMaterialType = optional.get();
        MaterialType newMaterialType = materialTypeRepository.save(materialType);
        ActionLogUtil.log(NAME, oldMaterialType, newMaterialType);
        return newMaterialType;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {
        // 验证是否存在
        Optional<MaterialType> optional = materialTypeRepository.findById(id);
        if (optional.isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }

        if(materialConsumptionItemRepository.findFirstByMaterialType(optional.get()) != null){
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_REF_KEY_EXISTS);
        }

        ActionLogUtil.log(NAME, 1, optional.get());
        materialTypeRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
        ActionLogUtil.log(NAME, 1, materialTypeRepository.findAllById(Arrays.asList(ids)));
        // 非系统值才可以删除
        materialTypeRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public MaterialType findOne(Integer id) {
        Optional<MaterialType> optional = materialTypeRepository.findById(id);
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
    public List<MaterialType> findAll() {
        return materialTypeRepository.findAll();
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
    public Page<MaterialType> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            MaterialType.class.getDeclaredField(sortFieldName);
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

        Pageable pageable = PageRequest.of(page, size, sort);
        return materialTypeRepository.findAll(pageable);
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
    public Page<MaterialType> findByNameLikeByPage(String name, Integer page, Integer size, String sortFieldName,
                                           Integer asc) {

        // 判断排序字段名是否存在
        try {
            MaterialType.class.getDeclaredField(sortFieldName);
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
        return materialTypeRepository.findByNameLike("%" + name + "%", pageable);
    }
}
