package com.hnu.fk.service;

import ch.qos.logback.core.joran.action.ActionUtil;
import com.hnu.fk.domain.SectionInfo;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.SectionInfoRepository;
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

/**工段信息
 * huxudong
 */
@Service
public class SectionInfoService {
    public static final String NAME = "工段基础信息";
    @Autowired
    private SectionInfoRepository sectionInfoRepository;

    /**
     * 新增
     */
    public SectionInfo add(SectionInfo sectionInfo){
        //判断是否存在
        if(sectionInfo==null
                ||(sectionInfo.getId()!=null&&sectionInfoRepository.findById(sectionInfo.getId()).isPresent()==true)){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }
        SectionInfo save = sectionInfoRepository.save(sectionInfo);
        ActionLogUtil.log(NAME,0,save);
        return save;
    }

    /**
     * 更新
     */
    public SectionInfo update(SectionInfo sectionInfo){
        Optional<SectionInfo> optional= null;
        //判断是否存在
        if(sectionInfo==null||sectionInfo.getId()==null||
                (optional = sectionInfoRepository.findById(sectionInfo.getId())).isPresent()==false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        SectionInfo oldValue = optional.get();
        SectionInfo newValue = sectionInfoRepository.save(sectionInfo);
        ActionLogUtil.log(NAME,oldValue,newValue);
        return newValue;
    }
    /**
     * 删除
     */
    public void delete(Integer id){
        Optional<SectionInfo> optional = sectionInfoRepository.findById(id);
        if(optional.isPresent()==false){
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        ActionLogUtil.log(NAME,1,optional.get());
        sectionInfoRepository.deleteById(id);
    }

    /**
     * 批量删除
     */
    @Transactional
    public void deleteInBatch(Integer[] ids){
        ActionLogUtil.log(NAME,1,sectionInfoRepository.findAllById(Arrays.asList(ids)));
        sectionInfoRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 查询
     */
    public SectionInfo findOne(Integer id){
        Optional<SectionInfo> optional = sectionInfoRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 查询所有
     */
    public List<SectionInfo> findAll(){
        return sectionInfoRepository.findAll();
    }

    /**
     * 查询所有-分页
     */
    public Page<SectionInfo> findAllByPage(Integer page,Integer size,String sortFieldName,Integer asc){
        try{
            SectionInfo.class.getDeclaredField(sortFieldName);
        }catch(Exception e){
            //如果不存在就设置为Id
            sortFieldName = "id";
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return sectionInfoRepository.findAll(pageable);
    }

    /**
     * 通过名称模糊查询-分页
     */
    public Page<SectionInfo> findNameLikeByPage(Integer page,Integer size,String sortFieldName,Integer asc,String name){
        try{
            SectionInfo.class.getDeclaredField(sortFieldName);
        }catch(Exception e){
            //如果不存在就设置为Id
            sortFieldName = "id";
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return sectionInfoRepository.findBySectionNameLike("%"+name+"%",pageable);
    }
}

