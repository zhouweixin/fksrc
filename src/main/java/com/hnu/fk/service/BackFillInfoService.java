package com.hnu.fk.service;

import com.hnu.fk.domain.BackFillInfo;
import com.hnu.fk.domain.MaintenanceSchedule;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.BackFillInfoRepository;
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
import java.util.List;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
@Service
public class BackFillInfoService {
    @Autowired
    private BackFillInfoRepository backFillInfoRepository;

    @Autowired
    private MaintenanceScheduleRepository maintenanceScheduleRepository;

    /**
     * 新增
     * @param backFillInfo
     * @return
     */
    public BackFillInfo save(BackFillInfo backFillInfo){
        if(backFillInfo ==null || (backFillInfo.getId()!=null && backFillInfoRepository.findById(backFillInfo.getId()).isPresent())){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getOne(backFillInfo.getMaintenanceSchedule().getId());
        maintenanceSchedule.setFlag(1);
        maintenanceScheduleRepository.save(maintenanceSchedule);

        ActionLogUtil.log("检修记录",0,backFillInfo);

        return backFillInfoRepository.save(backFillInfo);
    }

    /**
     * 修改
     * @param backFillInfo
     * @return
     */
    public BackFillInfo update(BackFillInfo backFillInfo){
        if(backFillInfo.getId()==null|| !backFillInfoRepository.findById(backFillInfo.getId()).isPresent()){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        ActionLogUtil.log("检修记录",backFillInfoRepository.getOne(backFillInfo.getId()),backFillInfo);

        return backFillInfoRepository.save(backFillInfo);
    }

    /**
     * 通过id批量删除
     * @param ids
     */
    @Transactional
    public void deleteInBatch(Integer[] ids){
        ActionLogUtil.log("检修记录",1,backFillInfoRepository.findAllById(Arrays.asList(ids)));
        backFillInfoRepository.deleteInBatch(backFillInfoRepository.findAllById(Arrays.asList(ids)));
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    public BackFillInfo findById(Integer id){
        if(backFillInfoRepository.findById(id).isPresent())
            return backFillInfoRepository.getOne(id);
        else
            throw new FkExceptions(EnumExceptions.SEARCH_FAILED_NOT_EXIST);
    }

    /**
     * 查询所有记录-分页
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<BackFillInfo> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            BackFillInfo.class.getDeclaredField(sortFieldName);
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
        return backFillInfoRepository.findAll(pageable);
    }

    /**
     * 通过时间段和设备查询
     * @param startDate
     * @param endDate
     * @param equipmentCode
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<BackFillInfo> getAllByEquipmentAndTime(String startDate, String endDate,String equipmentCode,Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            BackFillInfo.class.getDeclaredField(sortFieldName);
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
        return backFillInfoRepository.findByEnteringTimeAndEquipment(startDate,endDate,"%"+ equipmentCode + "%",pageable);
    }

    public List<BackFillInfo> getAllByEquipmentAndTime(String startDate, String endDate,String equipmentCode) {
        return backFillInfoRepository.findByEnteringTimeAndEquipment(startDate,endDate,"%"+ equipmentCode + "%");
    }
}
