package com.hnu.fk.service;

import com.hnu.fk.domain.MaintenanceSchedule;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.MaintenanceScheduleRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
@Service
public class MaintenanceScheduleService {
    @Autowired
    private MaintenanceScheduleRepository maintenanceScheduleRepository;


    /**
     * 新增
     * @param maintenanceSchedule
     * @return
     */
    public MaintenanceSchedule save(MaintenanceSchedule maintenanceSchedule){
        if(maintenanceSchedule ==null || (maintenanceSchedule.getId()!=null && maintenanceScheduleRepository.findById(maintenanceSchedule.getId()).isPresent())){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        Date currentTime = new Date();
        maintenanceSchedule.setEnteringTime(currentTime);

/*TODO
        User user = (User) SecurityUtils.getSubject().getSession(true).getAttribute("user");
        maintenanceSchedule.setEnter(user);
*/

        ActionLogUtil.log("检修计划",0,maintenanceSchedule);

        return maintenanceScheduleRepository.save(maintenanceSchedule);
    }

    /**
     * 修改
     * @param maintenanceSchedule
     * @return
     */
    public MaintenanceSchedule update(MaintenanceSchedule maintenanceSchedule){
        if(maintenanceSchedule.getId()==null|| !maintenanceScheduleRepository.findById(maintenanceSchedule.getId()).isPresent()){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        Date currentTime = new Date();
        maintenanceSchedule.setEnteringTime(currentTime);

/*TODO
        User user = (User) SecurityUtils.getSubject().getSession(true).getAttribute("user");
        maintenanceSchedule.setEnter(user);
*/

        ActionLogUtil.log("检修计划",maintenanceScheduleRepository.getOne(maintenanceSchedule.getId()),maintenanceSchedule);

        return maintenanceScheduleRepository.save(maintenanceSchedule);
    }
    /**
     * 通过id批量删除
     * @param ids
     */
    @Transactional
    public void deleteInBatch(Integer[] ids){
        ActionLogUtil.log("检修计划",1,maintenanceScheduleRepository.findAllById(Arrays.asList(ids)));
        maintenanceScheduleRepository.deleteInBatch(maintenanceScheduleRepository.findAllById(Arrays.asList(ids)));
    }

    /**
     * 查询所有计划-分页
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<MaintenanceSchedule> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            MaintenanceSchedule.class.getDeclaredField(sortFieldName);
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
        return maintenanceScheduleRepository.findAll(pageable);
    }

    /**
     * 查询所有计划
     * @return
     */
    public List<MaintenanceSchedule> findAll(Sort sort) {
        return maintenanceScheduleRepository.findAll(sort);
    }

    /**
     * 查询所有未完成计划-分页
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<MaintenanceSchedule> findAllNotCompleteByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            MaintenanceSchedule.class.getDeclaredField(sortFieldName);
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
        return maintenanceScheduleRepository.findAllByFlag(0,pageable);
    }

    /**
     * 通过设备名称和故障描述模糊查询
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<MaintenanceSchedule> findAllByNameAndDescriptionLike(String name, String description,Integer page, Integer size, String sortFieldName, Integer asc) {

        if(name == null){
            throw new FkExceptions(EnumExceptions.SEARCH_FAILED_NAME_NULL);
        }
        // 判断排序字段名是否存在
        try {
            MaintenanceSchedule.class.getDeclaredField(sortFieldName);
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
        if(description==null)
            return maintenanceScheduleRepository.findAllByEquipmentInfoId_NameLike("%" + name + "%",pageable);
        else
            return maintenanceScheduleRepository.findAllByEquipmentInfoId_NameLikeAndDescriptionLike("%" + name + "%","%" + description + "%",pageable);
    }
}

