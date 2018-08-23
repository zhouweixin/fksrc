package com.hnu.fk.service;

import com.hnu.fk.domain.StandingBookHeader;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.DataDictionaryRepository;
import com.hnu.fk.repository.StandingBookDetailRepository;
import com.hnu.fk.repository.StandingBookHeaderRepository;
import com.hnu.fk.repository.UserRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 台账调度管理表头Service
 */
@Service
public class StandingBookHeaderService {
    public static final String NAME = "台账调度管理表头";
    @Autowired
    private StandingBookDetailRepository standingBookDetailRepository;
    @Autowired
    private StandingBookHeaderRepository standingBookHeaderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;

    /**
     * 新增
     */
    public StandingBookHeader save(StandingBookHeader standingBookHeader) {
        //验证是否存在
        if (standingBookHeader == null ||
                (standingBookHeader.getId() != null && standingBookHeaderRepository.findById(standingBookHeader.getId()).isPresent() == true)) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }
        //判断是否已经录入
        if(standingBookHeaderRepository.findFirstByDateAndDataDictionary(standingBookHeader.getDate(),standingBookHeader.getDataDictionary())!=null){
            throw new FkExceptions(EnumExceptions.DATA_ENTERED);
        }
        //验证调度人是否存在
        if (standingBookHeader.getUser() == null || standingBookHeader.getUser().getId() == null
                || userRepository.findById(standingBookHeader.getUser().getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_USER_CODE_ENTER_NOT_EXISTS);
        }
        //验证是否存在班组
        if (standingBookHeader.getDataDictionary() == null || standingBookHeader.getDataDictionary().getId() == null
                || dataDictionaryRepository.findById(standingBookHeader.getDataDictionary().getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_BanCi_NOT_EXISTS);
        }
        //设置录入时间?
        //已经在实体自动生成日期和时间
        StandingBookHeader save = standingBookHeaderRepository.save(standingBookHeader);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }
    /**
     * 更新
     */
    @Transactional
    public StandingBookHeader update(StandingBookHeader standingBookHeader){
        Optional<StandingBookHeader> optional = null;
        //判断是否存在
        if(standingBookHeader==null||standingBookHeader.getId()==null||
                (optional=standingBookHeaderRepository.findById(standingBookHeader.getId())).isPresent()==false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        //删除旧数据
        standingBookDetailRepository.deleteByStandingBookHeader(optional.get());
        //设置不可修改部分
        //编号
        standingBookHeader.setStandingBook(optional.get().getStandingBook());
        //日期
        standingBookHeader.setDate(optional.get().getDate());
        //时间
        standingBookHeader.setTime(optional.get().getTime());
        //录入人
        standingBookHeader.setUser(optional.get().getUser());
        //所属班次
        standingBookHeader.setDataDictionary(optional.get().getDataDictionary());
        StandingBookHeader oldValue = optional.get();
        StandingBookHeader newValue = standingBookHeaderRepository.save(standingBookHeader);
        ActionLogUtil.log(NAME,oldValue,newValue);
        return newValue;
    }

    /**
     * 查询所有
     */
    public List<StandingBookHeader> findALL(){
        return standingBookHeaderRepository.findAll();
    }

    /**
     * 根据日期查询
     */
    public List<StandingBookHeader> findAllByDate(Date startDate, Date endDate){
        return standingBookHeaderRepository.findByDateBetween(startDate,endDate);
    }

    /**
     * 根据日期查询-分页
     */
    public Page<StandingBookHeader> findAllByDateByPage(Date startDate, Date endDate, Integer page, Integer size, String sortedFieldName, Integer asc){
        try{
            StandingBookHeader.class.getDeclaredField(sortedFieldName);
        }catch (Exception e){
            sortedFieldName = "id";
        }
        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortedFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortedFieldName);
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return standingBookHeaderRepository.findByDateBetween(startDate,endDate,pageable);
    }

    /**
     * 根据日期和班次查询
     */
    public List<StandingBookHeader> findAllByScheduleAndDate(Long scheduleId, Date startDate, Date endDate){
        return standingBookHeaderRepository.findByDataDictionary_IdAndDateBetween(scheduleId,startDate,endDate);
    }

    /**
     * 根据日期和班次查询-分页
     */
    public Page<StandingBookHeader> findAllBySchedulAndDateByPage(Long scheduleId, Date startDate, Date endDate, Integer page, Integer size, String sortedFieldName, Integer asc){
        try{
            StandingBookHeader.class.getDeclaredField(sortedFieldName);
        }catch (Exception e){
            sortedFieldName = "id";
        }
        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortedFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortedFieldName);
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return standingBookHeaderRepository.findByDataDictionary_IdAndDateBetween(scheduleId,startDate,endDate,pageable);
    }
    /**
     * 根据台账ids导出
     */
}
