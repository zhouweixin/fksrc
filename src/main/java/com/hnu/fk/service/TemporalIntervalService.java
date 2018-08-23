package com.hnu.fk.service;

import com.hnu.fk.domain.TemporalInterval;
import com.hnu.fk.domain.User;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.TemporalIntervalRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/13
 */
@Service
public class TemporalIntervalService {
    @Autowired
    private TemporalIntervalRepository temporalIntervalRepository;

    /**
     * 根据年份查询
     * @param statisticalYear
     * @return
     */
    public List<TemporalInterval> getAllByYear(String statisticalYear){
        return temporalIntervalRepository.findByStatisticalYearOrderByStatisticalMonthAsc(statisticalYear);
    }

    /**
     * 根据序号id查询
     * @param id
     * @return
     */
    public TemporalInterval getById(Long id){
        if(!temporalIntervalRepository.findById(id).isPresent()){
            throw new FkExceptions(EnumExceptions.SEARCH_FAILED_NOT_EXIST);
        }else
            return temporalIntervalRepository.getOne(id);
    }

    /**
     * 编辑
     * @param temporalInterval
     * @return
     */
    public TemporalInterval edit(TemporalInterval temporalInterval){
        /*User user = (User) SecurityUtils.getSubject().getSession(true).getAttribute("user");
        temporalInterval.setUpdateUser(user);
        */
        Date currentTime = new Date();
        temporalInterval.setUpdateTime(currentTime);
        if(temporalInterval.getId()!=null && temporalInterval.getId() == -1){
            ActionLogUtil.log("月统计时间区间表",0,temporalInterval);
            return temporalIntervalRepository.save(temporalInterval);
        } else if ( !temporalIntervalRepository.findById(temporalInterval.getId()).isPresent()){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        } else {
            ActionLogUtil.log("月统计时间区间表",temporalIntervalRepository.getOne(temporalInterval.getId()),temporalInterval);
            return temporalIntervalRepository.save(temporalInterval);
        }
    }
}
