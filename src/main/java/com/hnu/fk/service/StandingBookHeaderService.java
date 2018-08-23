package com.hnu.fk.service;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.hnu.fk.domain.SectionInfo;
import com.hnu.fk.domain.StandingBookHeader;
import com.hnu.fk.domain.StandingBookItem;
import com.hnu.fk.domain.StandingBookType;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.*;
import com.hnu.fk.utils.ActionLogUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 台账调度管理表头Service
 */
@Service
public class StandingBookHeaderService {
    public static final String NAME = "台账调度管理表头";
    private SimpleDateFormat standBookdate = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private StandingBookDetailRepository standingBookDetailRepository;
    @Autowired
    private StandingBookHeaderRepository standingBookHeaderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;
    @Autowired
    private SectionInfoRepository sectionInfoRepository;
    @Autowired
    private StandingBookItemRepository standingBookItemRepository;
    @Autowired
    private StandingBookTypeRepository standingBookTypeRepository;

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
        //录入时间自动生成
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
        //台账日期
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

/*    *//**
     * 导出台账
     *//*
    public Workbook exportByIds(Integer[] ids){
        //写日志
        ActionLogUtil.log(NAME);
        //查询工段是否存在
        List<SectionInfo> sectionInfos = sectionInfoRepository.findAll();
        if(sectionInfos==null||sectionInfos.size()==0){
            throw new FkExceptions(EnumExceptions.EXPORT_FAILED_FIELD_NULL);
        }
        //查询项目类别是否存在
        List<StandingBookType> standingBookTypes  = standingBookTypeRepository.findAll();
        if(standingBookTypes==null||standingBookTypes.size()==0){
            throw new FkExceptions(EnumExceptions.EXPORT_FAILED_FIELD_NULL);
        }
        //查询项目是否存在
        List<StandingBookItem> standingBookItems = standingBookItemRepository.findAll();
        if(standingBookItems==null||standingBookItems.size()==0){
            throw new FkExceptions(EnumExceptions.EXPORT_FAILED_FIELD_NULL);
        }

        //创建表
        List<ExcelExportEntity> entities = new ArrayList<>();
        // 【1、写表头】
        //1.编号
        ExcelExportEntity entity = new ExcelExportEntity("台账编号","standingBook");
        entities.add(entity);
        //2.导出日期
        entity = new ExcelExportEntity("日期","date");
        entity.setFormat("yyyy-MM-dd");
        entities.add(new ExcelExportEntity("日期", "date"));
        //3.班次
        entity = new ExcelExportEntity("班次","scheduleId");
        entities.add(entity);
        //4.调度人
        entity = new ExcelExportEntity("名字","name");
        entities.add(entity);
        //5,台账日期
        entity = new ExcelExportEntity("生成时间","standingBookDate");
        entity.setFormat("yyyy-MM-dd");
        entities.add(entity);
        //6,
    }*/

}
