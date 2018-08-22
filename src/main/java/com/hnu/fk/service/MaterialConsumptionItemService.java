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
import java.util.List;
import java.util.Optional;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 17:40 2018/8/20
 * @Modified By:
 */
@Service
public class MaterialConsumptionItemService {

    public static final String NAME = "物料";

    @Autowired
    private MaterialConsumptionItemRepository materialConsumptionItemRepository;

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    /**
     * 新增
     *
     * @param materialConsumptionItem
     * @return
     */
    public MaterialConsumptionItem save(MaterialConsumptionItem materialConsumptionItem) {

        // 验证是否存在
        if (materialConsumptionItem == null || (materialConsumptionItem.getId() != null && materialConsumptionItemRepository.findById(materialConsumptionItem.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        if (materialConsumptionItem.getMaterialType() == null || materialConsumptionItem.getMaterialType().getId() == null
                || materialTypeRepository.findById(materialConsumptionItem.getMaterialType().getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_MATERAIL_TYPE_NOT_EXISTS);
        }

        MaterialConsumptionItem save = materialConsumptionItemRepository.save(materialConsumptionItem);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新
     *
     * @param materialConsumptionItem
     * @return
     */
    public MaterialConsumptionItem update(MaterialConsumptionItem materialConsumptionItem) {

        // 验证是否存在
        Optional<MaterialConsumptionItem> optional = null;
        if (materialConsumptionItem == null || materialConsumptionItem.getId() == null || (optional = materialConsumptionItemRepository.findById(materialConsumptionItem.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        if (materialConsumptionItem.getMaterialType() == null || materialConsumptionItem.getMaterialType().getId() == null
                || materialTypeRepository.findById(materialConsumptionItem.getMaterialType().getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_MATERAIL_TYPE_NOT_EXISTS);
        }

        MaterialConsumptionItem oldMaterialConsumptionItem = optional.get();
        MaterialConsumptionItem newMaterialConsumptionItem = materialConsumptionItemRepository.save(materialConsumptionItem);
        ActionLogUtil.log(NAME, oldMaterialConsumptionItem, newMaterialConsumptionItem);
        return newMaterialConsumptionItem;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {
        // 验证是否存在
        Optional<MaterialConsumptionItem> optional = materialConsumptionItemRepository.findById(id);
        if (optional.isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }

        ActionLogUtil.log(NAME, 1, optional.get());
        materialConsumptionItemRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
        ActionLogUtil.log(NAME, 1, materialConsumptionItemRepository.findAllById(Arrays.asList(ids)));
        // 非系统值才可以删除
        materialConsumptionItemRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public MaterialConsumptionItem findOne(Integer id) {
        Optional<MaterialConsumptionItem> optional = materialConsumptionItemRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<MaterialConsumptionItem> findAll() {
        return materialConsumptionItemRepository.findAll(Sort.by("id"));
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
    public Page<MaterialConsumptionItem> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            MaterialConsumptionItem.class.getDeclaredField(sortFieldName);
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
        return materialConsumptionItemRepository.findAll(pageable);
    }

    /**
     * 通过物料类型和名称模糊查询-分页
     *
     * @param materialTypeId
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<MaterialConsumptionItem> getByMaterialTypeAndNameLikeByPage(Integer materialTypeId, String name, Integer page, Integer size, String sortFieldName,
                                                                            Integer asc) {

        // 判断排序字段名是否存在
        try {
            MaterialConsumptionItem.class.getDeclaredField(sortFieldName);
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
        if(materialTypeId == -1){
            return materialConsumptionItemRepository.findByNameLike("%" + name + "%", pageable);
        }

        return materialConsumptionItemRepository.findByMaterialTypeAndNameLike(new MaterialType(materialTypeId), "%" + name + "%", pageable);
    }
}
