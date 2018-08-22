package com.hnu.fk.service;

import com.hnu.fk.domain.StandingBookItem;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.StandingBookItemRepository;
import com.hnu.fk.repository.StandingBookTypeRepository;
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
 * 调度台账项目Service
 */
@Service
public class StandingBookItemService {
    public static final String NAME = "调度台账项目";
    @Autowired
    private StandingBookItemRepository standingBookItemRepository;
    @Autowired
    private StandingBookTypeRepository standingBookTypeRepository;
     /**
     * 新增
     */
     public StandingBookItem add(StandingBookItem standingBookItem){
         //判断是否存在
         if(standingBookItem==null||(standingBookItem.getId()!=null&&standingBookItemRepository.findById(standingBookItem.getId()).isPresent())==true){
             throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
         }
         //判断外键是否存在
         if(standingBookItem.getStandingBookType()==null||
                 standingBookItem.getStandingBookType().getId()==null||
                 standingBookTypeRepository.findById(standingBookItem.getStandingBookType().getId()).isPresent()==false){
             throw new FkExceptions(EnumExceptions.ADD_FAILED_STANDING_BOOK_TYPE_NOT_EXISTS);
         }
         StandingBookItem save = standingBookItemRepository.save(standingBookItem);
         ActionLogUtil.log(NAME,0,save);
         return save;
     }
    /**
     * 更新
     */
    public StandingBookItem update(StandingBookItem standingBookItem){
        Optional<StandingBookItem> optional = null;
        //判断是否存在
        if(standingBookItem==null||
                standingBookItem.getId()==null||
                (optional=standingBookItemRepository.findById(standingBookItem.getId())).isPresent()==false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        //判断外键是否存在
        if(standingBookItem.getStandingBookType()==null||standingBookItem.getStandingBookType().getId()==null
                ||standingBookTypeRepository.findById(standingBookItem.getStandingBookType().getId()).isPresent()==false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_STANDING_BOOK_TYPE_NOT_EXISTS);
        }
        StandingBookItem oldValue = optional.get();
        StandingBookItem newValue = standingBookItemRepository.save(standingBookItem);
        ActionLogUtil.log(NAME,oldValue,newValue);
        return newValue;
    }
    /**
     * 删除
     */
    public void delete(Integer id){
        //验证是否存在
        Optional<StandingBookItem> optional=standingBookItemRepository.findById(id);
        if(optional.isPresent()==false){
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        ActionLogUtil.log(NAME,1,optional.get());
        standingBookItemRepository.deleteById(id);
    }
    /**
     * 批量删除
     */
    @Transactional
    public void deleteInBatch(Integer[] ids){
        ActionLogUtil.log(NAME,1,standingBookItemRepository.findAllById(Arrays.asList(ids)));
        standingBookItemRepository.deleteByIdIn(Arrays.asList(ids));
    }
    /**
     * 根据id查询
     */
    public StandingBookItem findOne(Integer id){
        Optional<StandingBookItem> optional = standingBookItemRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }
    /**
     * 查询所有
     */
    public List<StandingBookItem> findAll(){
        return standingBookItemRepository.findAll();
    }
    /**
     * 查询所有-分页
     */
    public Page<StandingBookItem> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            StandingBookItem.class.getDeclaredField(sortFieldName);
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
        return standingBookItemRepository.findAll(pageable);
    }
    /**
     *通过项目类别和名称模糊查询-分页
     */
    public Page<StandingBookItem> findByNameLikeAndTypeByPage(Integer page, Integer size, String sortFieldName, Integer asc,String itemName,Integer id){

        // 判断排序字段名是否存在
        try {
            StandingBookItem.class.getDeclaredField(sortFieldName);
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
        return standingBookItemRepository.findByItemNameLikeAndStandingBookTypeId("%"+itemName+"%",id,pageable);
    }
}
