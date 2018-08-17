package com.hnu.fk.service;

import com.hnu.fk.domain.DataDictionary;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.DataDictionaryRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @Author: huXuDong
 * @Description: 数据字典
 * @Date: Created in 10:20 2018/8/15
 * @Modified By:
 */

@Service
public class DataDictionaryService {
    public static final String NAME = "数据字典";
    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;

    /**
     * 新增字典
     *
     * @param dataDictionary
     * @return
     */
    public DataDictionary save(DataDictionary dataDictionary) {
        //验证是否存在
        if (dataDictionary == null || (dataDictionary.getId() != null && dataDictionaryRepository.findById(dataDictionary.getId()).isPresent() == true)) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }
        //判断是类型还是数据
        if (dataDictionary.getDicParentId() != -1 &&
                dataDictionaryRepository.findFirstByDicId(dataDictionary.getDicParentId()) == null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DATAPARENT_NOT_EXISTS);
        }
        //编号重复
        if (dataDictionaryRepository.findFirstByDicId(dataDictionary.getDicId()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICID_DUPLICATE);
        }
        //名称重复
        if (dataDictionaryRepository.findFirstByDicName(dataDictionary.getDicName()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICNAME_DUPLICATE);
        }
        //值重复
        if (dataDictionaryRepository.findFirstByDicContent(dataDictionary.getDicContent()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICCONTENT_DUPLICATE);
        }
        Integer max = dataDictionaryRepository.maxRank();
        if (max == null) {
            max = 0;
        }
        dataDictionary.setRank(max + 1);
        DataDictionary save = dataDictionaryRepository.save(dataDictionary);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新字典
     *
     * @param dataDictionary
     * @return
     */
    public DataDictionary update(DataDictionary dataDictionary) {
        Optional<DataDictionary> optional = null;
        //验证是否存在
        if (dataDictionary == null || dataDictionary.getId() == null ||
                (optional = dataDictionaryRepository.findById(dataDictionary.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        //判断是类型还是数据
        if (dataDictionary.getDicParentId() != -1 &&
                dataDictionaryRepository.findFirstByDicId(dataDictionary.getDicParentId()) == null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DATAPARENT_NOT_EXISTS);
        }
        //编号重复
        if (dataDictionaryRepository.findFirstByIdNotAndDicId(dataDictionary.getId(), dataDictionary.getDicId()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICID_DUPLICATE);
        }
        //名称重复
        if (dataDictionaryRepository.findFirstByIdNotAndDicName(dataDictionary.getId(), dataDictionary.getDicName()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICNAME_DUPLICATE);
        }
        //值重复
        if (dataDictionaryRepository.findFirstByIdNotAndDicContent(dataDictionary.getId(), dataDictionary.getDicContent()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICCONTENT_DUPLICATE);
        }
        DataDictionary oldValue = optional.get();
        DataDictionary newValue = dataDictionaryRepository.save(dataDictionary);
        ActionLogUtil.log(NAME, oldValue, newValue);

        return newValue;
    }

    /**
     * 删除字典,删除类型会删除其下所有数据
     *
     * @param id
     */
    @Transactional
    public void delete(Long id) {
        //验证是否存在
        Optional<DataDictionary> optional = dataDictionaryRepository.findById(id);
        if (optional == null) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        //判断是否为父类
        if (optional.get().getDicParentId() == -1) {
            List<DataDictionary> dataDictionaries = dataDictionaryRepository.findAllByDicParentId(optional.get().getDicId());
            List<Long> dataIds = new ArrayList<>();
            for (DataDictionary dataDictionary : dataDictionaries) {
                dataIds.add(dataDictionary.getId());
            }
            dataIds.add(id);
            ActionLogUtil.log(NAME, 1, dataDictionaryRepository.findAllById(dataIds));
            dataDictionaryRepository.deleteByIdIn(dataIds);
        } else {
            ActionLogUtil.log(NAME, 1, optional.get());
            dataDictionaryRepository.deleteById(id);
        }
    }

    /**
     * 批量删除字典
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    /**
     * 根据id查询字典
     *
     * @param id
     * @return
     */
    public DataDictionary findOne(Long id) {
        return dataDictionaryRepository.findById(id).get();
    }

    /**
     * 根据编码查询字典
     */
    public DataDictionary findOneByDicId(Integer dicId,Integer parentId){
        return dataDictionaryRepository.findByDicIdAndDicParentId(dicId,parentId);
    }

    /**
     * 根据类型编码查询其所有数据
     * @param dicId
     * @return
     */
    public List<DataDictionary> findAllChildrenById(Integer dicId) {
        return dataDictionaryRepository.findAllByDicParentId(dicId);
    }

    /**
     * 查询所有类型
     *
     * @return
     */
    public List<DataDictionary> findAllParents() {
        return dataDictionaryRepository.findAllByDicParentId(-1);
    }

    /**
     * 根据类型编码分页查询其所有数据
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @param dicId
     * @return
     */
        public Page<DataDictionary> findAllChildrensByPageById(Integer page, Integer size, String sortFieldName, Integer asc, Integer dicId) {
        // 判断排序字段名是否存在
        try {
            DataDictionary.class.getDeclaredField(sortFieldName);
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
        return dataDictionaryRepository.findAllByDicParentId(pageable, dicId);
    }

    /**
     * 根据类型编码模糊查询其下所有数据-分页
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @param dicId
     * @param name
     * @return
     */
    public Page<DataDictionary> findChildrensByPageNameLikeById(Integer page, Integer size,
                                                                String sortFieldName, Integer asc, Integer dicId, String name) {
        //判断字段名是否存在
        try {
            DataDictionary.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            sortFieldName = "id";
        }
        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return dataDictionaryRepository.findByDicNameLikeAndDicParentId(pageable, "%" + name + "%", dicId);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<DataDictionary> findAll() {
        return dataDictionaryRepository.findAll();
    }

    /**
     * 分页查询所有类型
     *
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<DataDictionary> findAllParentsByPage(Integer page, Integer size, String sortFieldName, Integer asc) {
        //判断排序字段名是否存在
        try {
            DataDictionary.class.getField(sortFieldName);
        } catch (Exception e) {
            sortFieldName = "id";
        }
        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return dataDictionaryRepository.findAll(pageable);
    }
}
