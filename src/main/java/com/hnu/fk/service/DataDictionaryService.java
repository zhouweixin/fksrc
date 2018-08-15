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
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * HuXuDong
 *
 *
 */
@Service
public class DataDictionaryService {
    public static final String NAME= "数据字典";
    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;
    /**
     * 新增字典
     */
    public DataDictionary save(DataDictionary dataDictionary){
        //验证是否存在
        if(dataDictionary==null || (dataDictionary.getId()!=null&&dataDictionaryRepository.findById(dataDictionary.getId()).isPresent()==true)){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }
        //判断是类型还是数据
        if(dataDictionary.getDicParentId()!=-1&&
                dataDictionaryRepository.findByDicId(dataDictionary.getDicParentId())==null){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DATAPARENT_NOT_EXISTS);
        }
        //编号重复
        if(dataDictionaryRepository.findByDicId(dataDictionary.getDicId())!=null){
            throw  new FkExceptions(EnumExceptions.ADD_FAILED_DICID_DUPLICATE);
        }
        //名称重复
        if(dataDictionaryRepository.findByDicName(dataDictionary.getDicName())!=null){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICNAME_DUPLICATE);
        }
        //值重复
        if(dataDictionaryRepository.findByDicContent(dataDictionary.getDicContent())!=null){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICCONTENT_DUPLICATE);
        }
        Integer max = dataDictionaryRepository.maxRank();
        if(max==null){
            max=0;
        }
        dataDictionary.setRank(max+1);
        DataDictionary save = dataDictionaryRepository.save(dataDictionary);
        ActionLogUtil.log(NAME,0,save);
        return save;
    }
    /**
     * 更新字典     子父类都是一个更新
     */
    public DataDictionary update(DataDictionary dataDictionary){
        Optional<DataDictionary> optional = null;
        //验证是否存在
        if(dataDictionary==null||dataDictionary.getId()==null||
                (optional = dataDictionaryRepository.findById(dataDictionary.getId())).isPresent()==false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        //判断是类型还是数据
        if(dataDictionary.getDicParentId()!=-1&&
                dataDictionaryRepository.findByDicId(dataDictionary.getDicParentId())==null){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DATAPARENT_NOT_EXISTS);
        }
        //编号重复
        if(dataDictionaryRepository.findFirstByIdNotAndDicId(dataDictionary.getId(),dataDictionary.getDicId())!=null){
            throw  new FkExceptions(EnumExceptions.ADD_FAILED_DICID_DUPLICATE);
        }
        //名称重复
        if(dataDictionaryRepository.findFirstByIdNotAndDicName(dataDictionary.getId(),dataDictionary.getDicName())!=null){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICNAME_DUPLICATE);
        }
        //值重复
        if(dataDictionaryRepository.findFirstByIdNotAndDicContent(dataDictionary.getId(),dataDictionary.getDicContent())!=null){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DICCONTENT_DUPLICATE);
        }
        DataDictionary oldValue = optional.get();
        DataDictionary newValue = dataDictionaryRepository.save(dataDictionary);
        ActionLogUtil.log(NAME,oldValue,newValue);

        return newValue;
    }
    /**
     * 删除字典(删除父类会连带删除父类及其下所有子类)
     * 删除子类正常删除
     */
    @Transactional
    public void delete(Long id){
        //验证是否存在
        Optional<DataDictionary> optional = dataDictionaryRepository.findById(id);
        if(optional==null){
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        //判断是否为父类
        if(optional.get().getDicParentId()==-1){
            List<DataDictionary> dataDictionaries = dataDictionaryRepository.findAllByDicParentId(optional.get().getDicId());
            List<Long> dataIds = new ArrayList<>();
            for(DataDictionary dataDictionary : dataDictionaries){
                dataIds.add(dataDictionary.getId());
            }
            dataIds.add(id);
            ActionLogUtil.log(NAME,1,dataDictionaryRepository.findAllById(dataIds));
            dataDictionaryRepository.deleteByIdIn(dataIds);
        }
        else {
            ActionLogUtil.log(NAME,1,optional.get());
            dataDictionaryRepository.deleteById(id);
        }
    }

    /**
     * 批量删除字典
     */
    @Transactional
    public void deleteByIdIn(Long[] ids){
        for(Long id : ids){
            delete(id);
        }
    }

    /**
     * 根据id查询
     */
    public DataDictionary findOne(Long id){
        return dataDictionaryRepository.findById(id).get();
    }

    /**
     * 根据父类id查询其子类
     */
    public List<DataDictionary>  findAllChildrenById(Long id){
        Integer dicId = dataDictionaryRepository.findById(id).get().getDicId();
        return dataDictionaryRepository.findAllByDicParentId(dicId);
    }
    /**
     * 查询所有类型
     */
    public List<DataDictionary> findAllParents(){
        return dataDictionaryRepository.findAllByDicParentId(-1);
    }
    /**
     * 子类分页查询所有
     */
    public Page<DataDictionary> findAllChildrensByPageById(Integer page,Integer size,String sortFieldName,Integer asc,Long id ){
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

        Pageable pageable =PageRequest.of(page, size, sort);
        Integer dicId = dataDictionaryRepository.findById(id).get().getDicId();
        return dataDictionaryRepository.findAllByDicParentId(pageable,dicId);
    }
    /**
     * 子类分页模糊查询
     */
    public Page<DataDictionary> findChildrensByPageNameLikeById(Integer page,Integer size,
                                                                  String sortFieldName,Integer asc,Long id, String name){
        //判断字段名是否存在
        try{DataDictionary.class.getDeclaredField(sortFieldName);}
        catch (Exception e){
            sortFieldName = "id";
        }
        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }
        Pageable pageable =PageRequest.of(page, size, sort);
        Integer dicParentId = dataDictionaryRepository.findById(id).get().getDicId();
        return dataDictionaryRepository.findByDicNameLikeAndDicParentId(pageable,"%"+name+"%",dicParentId);
    }
    /**
     * 查询所有
     */
    public List<DataDictionary> findAll(){
        return dataDictionaryRepository.findAll();
    }
    /**
     * 分页查询所有类型
     */
    public Page<DataDictionary> findAllParentsByPage(Integer page,Integer size,String sortFieldName, Integer asc){
        //判断排序字段名是否存在
        try{DataDictionary.class.getField(sortFieldName);
        } catch (Exception e) {
            sortFieldName = "id";
        }
        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }
        Pageable pageable = PageRequest.of(page,size,sort);
        return dataDictionaryRepository.findAll(pageable);
    }
}
