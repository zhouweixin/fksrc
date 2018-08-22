package com.hnu.fk.service;

import com.hnu.fk.domain.DataDictionary;
import com.hnu.fk.domain.DataDictionaryType;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.DataDictionaryRepository;
import com.hnu.fk.repository.DataDictionaryTypeRepository;
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
    public static final String TYPENAME = "数据字典类型";
    public static final String DATANAME = "数据字典数据";
    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;
    @Autowired
    private DataDictionaryTypeRepository dataDictionaryTypeRepository;

    /**
     * 新增数据字典类型
     *
     * @param dataDictionaryType
     * @return
     */
    public DataDictionaryType saveType(DataDictionaryType dataDictionaryType) {

        //验证是否存在
        if (dataDictionaryType == null ||
                (dataDictionaryType.getId() != null && dataDictionaryRepository.findById(dataDictionaryType.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        //验证值是否同名
        if (dataDictionaryTypeRepository.findFirstByTypeValue(dataDictionaryType.getTypeValue()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICVALUE_DUPLICATE);
        }

        //自动填入排序
        Integer max = dataDictionaryTypeRepository.maxRank();
        if (max == null) {
            max = 0;
        }
        dataDictionaryType.setRank(max + 1);

        DataDictionaryType save = dataDictionaryTypeRepository.save(dataDictionaryType);
        ActionLogUtil.log(TYPENAME, 0, save);
        return save;

    }

    /**
     * 更新数据字典类型
     */
    public DataDictionaryType updateType(DataDictionaryType dataDictionaryType) {
        Optional<DataDictionaryType> optional = null;
        //验证是否存在
        if (dataDictionaryType == null || dataDictionaryType.getId() == null ||
                (optional = dataDictionaryTypeRepository.findById(dataDictionaryType.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        //验证值是否同名
        if (dataDictionaryTypeRepository.findFirstByIdNotAndTypeName(dataDictionaryType.getId(),dataDictionaryType.getTypeValue()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICVALUE_DUPLICATE);
        }
        DataDictionaryType oldValue = optional.get();
        //自动保留以前的rank
        if (dataDictionaryType.getRank()==null){
            dataDictionaryType.setRank(oldValue.getRank());
        }
        DataDictionaryType newValue = dataDictionaryTypeRepository.save(dataDictionaryType);
        ActionLogUtil.log(TYPENAME, oldValue, newValue);

        return newValue;
    }

    /**
     * 删除数据字典类型
     */
    public void deleteType(Long id) {
        // 验证是否存在
        if (dataDictionaryTypeRepository.findById(id).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        ActionLogUtil.log(TYPENAME, 1, dataDictionaryTypeRepository.getOne(id));

        dataDictionaryTypeRepository.deleteById(id);
    }

    /**
     * 批量删除数据字典类型
     */
    @Transactional
    public void deleteTypeInbatch(Long[] ids) {
        List<DataDictionary> dataDictionaries = dataDictionaryRepository.findAllByDataDictionaryTypeIdIn(Arrays.asList(ids));
        ActionLogUtil.log(TYPENAME, 1, dataDictionaryTypeRepository.findAllById(Arrays.asList(ids)));
        ActionLogUtil.log(DATANAME,1,dataDictionaries);
        dataDictionaryTypeRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 根据id查询数据字典类型
     */
    public DataDictionaryType findOneType(Long id) {
        return dataDictionaryTypeRepository.findById(id).get();
    }

    /**
     * 查询所有数据字典类型
     *
     * @return
     */
    public List<DataDictionaryType> findAllTypes() {
        return dataDictionaryTypeRepository.findAll();
    }

    /**
     * 查询所有数据字典类型-分页
     */
    public Page<DataDictionaryType> findAllTypesByPage(Integer page, Integer size, String sortFieldName, Integer asc) {
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
        return dataDictionaryTypeRepository.findAll(pageable);
    }

    /**
     * 模糊查询所有数据字典类型-分页
     */
    public Page<DataDictionaryType> findAllTypeNameLikeByPage(Integer page, Integer size, String sortFieldName, Integer asc, String typeName) {
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
        return dataDictionaryTypeRepository.findByTypeNameLike("%"+typeName+"%", pageable);
    }

    /**
     * 新增数据字典数据
     */
    public DataDictionary saveData(DataDictionary dataDictionary) {
        //验证是否存在
        if (dataDictionary == null || (dataDictionary.getId() != null && dataDictionaryRepository.findById(dataDictionary.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }
        //验证是否存在类型
        if (dataDictionary.getDataDictionaryType() == null || dataDictionaryTypeRepository.findById(dataDictionary.getDataDictionaryType().getId()) == null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DATAPARENT_NOT_EXISTS);
        }
        //值重复
        if (dataDictionaryRepository.findFirstByDicContentAndDataDictionaryTypeId(dataDictionary.getDicContent(), dataDictionary.getDataDictionaryType().getId()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICVALUE_DUPLICATE);
        }

        //自动填入排序
        Integer max = dataDictionaryRepository.maxRank();
        if (max == null) {
            max = 0;
        }
        dataDictionary.setRank(max + 1);

        DataDictionary save = dataDictionaryRepository.save(dataDictionary);
        ActionLogUtil.log(DATANAME, 0, save);
        return save;
    }

    /**
     * 更新数据字典数据
     *
     * @param dataDictionary
     * @return
     */
    public DataDictionary updateData(DataDictionary dataDictionary) {
        Optional<DataDictionary> optional = null;
        //验证是否存在
        if (dataDictionary == null || dataDictionary.getId() == null ||
                (optional = dataDictionaryRepository.findById(dataDictionary.getId())).isPresent() == false
                ) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        //验证是否存在类型
        if (dataDictionary.getDataDictionaryType() == null || dataDictionaryTypeRepository.findById(dataDictionary.getDataDictionaryType().getId()) == null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DATAPARENT_NOT_EXISTS);
        }
        //值重复
        if (dataDictionaryRepository.findFirstByIdNotAndDicContentAndDataDictionaryTypeId(dataDictionary.getId(), dataDictionary.getDicContent(),dataDictionary.getDataDictionaryType().getId()) != null) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICVALUE_DUPLICATE);
        }

        DataDictionary oldValue = optional.get();
        if (dataDictionary.getRank()==null){
            dataDictionary.setRank(oldValue.getRank());
        }
        DataDictionary newValue = dataDictionaryRepository.save(dataDictionary);

        ActionLogUtil.log(DATANAME, oldValue, newValue);

        return newValue;
    }

    /**
     * 删除字典数据
     *
     * @param id
     */
    public void deleteData(Long id) {
        //验证是否存在
        Optional<DataDictionary> optional = dataDictionaryRepository.findById(id);
        if (optional.isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
            ActionLogUtil.log(DATANAME, 1, optional.get());
            dataDictionaryRepository.deleteById(id);
        }


    /**
     * 批量删除字典
     *
     * @param ids
     */
    @Transactional
    public void deleteDataInBatch(Long[] ids) {
            ActionLogUtil.log(DATANAME, 1,dataDictionaryRepository.findAllById(Arrays.asList(ids)));
            dataDictionaryRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 根据id查询字典
     *
     * @param id
     * @return
     */
    public DataDictionary findOneData(Long id) {
        return dataDictionaryRepository.findById(id).get();
    }


    /**
     * 根据类型主键查询其所有数据
     *
     * @param id
     * @return
     */
    public List<DataDictionary> findAllDataByTypeId(Long id) {
        return dataDictionaryRepository.findAllByDataDictionaryTypeId(id);
    }

    /**
     * 根据类型主键查询其所有数据-分页
     *
     */
    public Page<DataDictionary> findAllDataByPageByTypeId(Integer page, Integer size, String sortFieldName, Integer asc, Long id) {
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
        return dataDictionaryRepository.findAllByDataDictionaryTypeId(id,pageable);
    }

    /**
     * 根据类型主键模糊查询其下所有数据-分页
     *
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @param id
     * @param name
     * @return
     */
    public Page<DataDictionary> findDataByPageNameLikeById(Integer page, Integer size,
                                                           String sortFieldName, Integer asc, Long id, String name) {
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
        return dataDictionaryRepository.findAllByDataDictionaryTypeIdAndDicNameLike(id, "%" + name + "%", pageable);
    }

}
