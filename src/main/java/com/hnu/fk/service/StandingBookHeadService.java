package com.hnu.fk.service;

import ch.qos.logback.core.joran.action.ActionUtil;
import com.hnu.fk.domain.DataDictionary;
import com.hnu.fk.domain.StandingBookHead;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.DataDictionaryRepository;
import com.hnu.fk.repository.StandingBookDetailRepository;
import com.hnu.fk.repository.StandingBookHeadRepository;
import com.hnu.fk.repository.UserRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.apache.poi.ss.usermodel.Workbook;
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
public class StandingBookHeadService {
    public static final String NAME = "台账调度管理表头";
    @Autowired
    private StandingBookDetailRepository standingBookDetailRepository;
    @Autowired
    private StandingBookHeadRepository standingBookHeadRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;

    /**
     * 新增
     */
    public StandingBookHead save(StandingBookHead standingBookHead) {
        //验证是否存在
        if (standingBookHead == null ||
                (standingBookHead.getId() != null && standingBookHeadRepository.findById(standingBookHead.getId()).isPresent() == true)) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }
        //验证调度人是否存在
        if (standingBookHead.getUser() == null || standingBookHead.getUser().getId() == null
                || userRepository.findById(standingBookHead.getUser().getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_USER_CODE_ENTER_NOT_EXISTS);
        }
        //验证是否存在班组
        if (standingBookHead.getDataDictionary() == null || standingBookHead.getDataDictionary().getId() == null
                || dataDictionaryRepository.findById(standingBookHead.getDataDictionary().getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_SSBZ_NOT_EXISTS);
        }
        /**
         * 如果当天当班的生产调度台账已经生成过
         * 系统会给出提示,去历史记录查看？？
         */
        //设置录入时间?
        StandingBookHead save = standingBookHeadRepository.save(standingBookHead);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 编辑
     */
/*    @Transactional
    public StandingBookHead update(StandingBookHead standingBookHead) {
        Optional<StandingBookHead> optional = null;
        //验证是否存在
        if(standingBookHead==null||standingBookHead.getId()==null
                ||(optional=standingBookHeadRepository.findById(standingBookHead.getId())).isPresent()==false){
            throw  new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST)
        }
    }*/

    /**
     * 查询所有
     */
    public List<StandingBookHead> findALL(){
        return standingBookHeadRepository.findAll();
    }

    /**
     * 根据日期查询
     */
    public List<StandingBookHead> findAllByDate(Date startDate, Date endDate){
        return standingBookHeadRepository.findByDateBetween(startDate,endDate);
    }

    /**
     * 根据日期查询-分页
     */
    public Page<StandingBookHead> findAllByDateByPage(Date startDate, Date endDate, Integer page, Integer size, String sortedFieldName, Integer asc){
        try{
            StandingBookHead.class.getDeclaredField(sortedFieldName);
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
        return standingBookHeadRepository.findByDateBetween(startDate,endDate,pageable);
    }

    /**
     * 根据日期和班次查询
     */
    public List<StandingBookHead> findAllByBzAndDate(Integer bzId,Date startDate,Date endDate){
        return standingBookHeadRepository.findBySsbzIdAndDateBetween(bzId,startDate,endDate);
    }

    /**
     * 根据日期和班次查询-分页
     */
    public Page<StandingBookHead> findAllByBzDateByPage(Integer bzId,Date startDate, Date endDate, Integer page, Integer size, String sortedFieldName, Integer asc){
        try{
            StandingBookHead.class.getDeclaredField(sortedFieldName);
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
        return standingBookHeadRepository.findBySsbzIdAndDateBetween(bzId,startDate,endDate,pageable);
    }

    /**
     * 根据日期导出
     */
}
