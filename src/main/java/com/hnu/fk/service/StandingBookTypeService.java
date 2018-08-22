package com.hnu.fk.service;

import com.hnu.fk.domain.StandingBookType;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.SectionInfoRepository;
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
 * 台账项目类别Service
 */
@Service
public class StandingBookTypeService {
    public static final String NAME = "台账项目类别";
    @Autowired
    private SectionInfoRepository sectionInfoRepository;
    @Autowired
    private StandingBookTypeRepository standingBookTypeRepository;
    /**
     * 新增
     */
    public StandingBookType add(StandingBookType standingBookType){
        //判断是否存在
        if(standingBookType==null||(standingBookType.getId()!=null&&standingBookTypeRepository.findById(standingBookType.getId()).isPresent())==true){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }
        //判断外键是否存在
        if(standingBookType.getSectionInfo()==null||
                standingBookType.getSectionInfo().getId()==null||
                sectionInfoRepository.findById(standingBookType.getSectionInfo().getId()).isPresent()==false){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_SECTION_NOT_EXISTS);
        }
        StandingBookType save = standingBookTypeRepository.save(standingBookType);
        ActionLogUtil.log(NAME,0,save);
        return save;
    }
    /**
     * 更新
     */
    public StandingBookType update(StandingBookType standingBookType){
        Optional<StandingBookType> optional = null;
        //判断是否存在
        if(standingBookType==null||
                standingBookType.getId()==null||
                (optional=standingBookTypeRepository.findById(standingBookType.getId())).isPresent()==false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        //判断外键是否存在
        if(standingBookType.getSectionInfo()==null||standingBookType.getSectionInfo().getId()==null
                ||standingBookTypeRepository.findById(standingBookType.getSectionInfo().getId()).isPresent()==false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_SECTION_NOT_EXISTS);
        }
        StandingBookType oldValue = optional.get();
        StandingBookType newValue = standingBookTypeRepository.save(standingBookType);
        ActionLogUtil.log(NAME,oldValue,newValue);
        return newValue;
    }
    /**
     * 删除
     */
    public void delete(Integer id){
        //验证是否存在
        Optional<StandingBookType> optional=standingBookTypeRepository.findById(id);
        if(optional.isPresent()==false){
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        ActionLogUtil.log(NAME,1,optional.get());
        standingBookTypeRepository.deleteById(id);
    }
    /**
     * 批量删除
     */
    @Transactional
    public void deleteInBatch(Integer[] ids){
        ActionLogUtil.log(NAME,1,standingBookTypeRepository.findAllById(Arrays.asList(ids)));
        standingBookTypeRepository.deleteByIdIn(Arrays.asList(ids));
    }
    /**
     * 根据id查询
     */
    public StandingBookType findOne(Integer id){
        Optional<StandingBookType> optional = standingBookTypeRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }
    /**
     * 查询所有
     */
    public List<StandingBookType> findAll(){
        return standingBookTypeRepository.findAll();
    }
    /**
     * 查询所有-分页
     */
    public Page<StandingBookType> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            StandingBookType.class.getDeclaredField(sortFieldName);
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
        return standingBookTypeRepository.findAll(pageable);
    }
    /**
     *通过工段和名称模糊查询-分页
     */
    public Page<StandingBookType> findByNameLikeAndSectionByPage(Integer page, Integer size, String sortFieldName, Integer asc,String itemTypeName,Integer id){

        // 判断排序字段名是否存在
        try {
            StandingBookType.class.getDeclaredField(sortFieldName);
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
        return standingBookTypeRepository.findByItemTypeNameLikeAndSectionInfoId("%"+itemTypeName+"%",id,pageable);
    }
}
