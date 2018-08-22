package com.hnu.fk.service;

import ch.qos.logback.core.joran.action.ActionUtil;
import com.hnu.fk.domain.EquipmentInfo;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.EquipmentInfoRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
@Service
public class EquipmentInfoService {
    @Autowired
    private EquipmentInfoRepository equipmentInfoRepository;

    /**
     * 新增
     * @param equipmentInfo
     * @return
     */
    public EquipmentInfo save(EquipmentInfo equipmentInfo){
        if(equipmentInfo ==null || (equipmentInfo.getId()!=null && equipmentInfoRepository.findById(equipmentInfo.getId()).isPresent())){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        ActionLogUtil.log("设备信息",0,equipmentInfo);

        return equipmentInfoRepository.save(equipmentInfo);
    }

    /**
     * 修改
     * @param equipmentInfo
     * @return
     */
    public EquipmentInfo update(EquipmentInfo equipmentInfo){
        if(equipmentInfo.getId()==null|| !equipmentInfoRepository.findById(equipmentInfo.getId()).isPresent()){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        EquipmentInfo equipmentInfoOld = equipmentInfoRepository.getOne(equipmentInfo.getId());
        ActionLogUtil.log("设备信息",equipmentInfoRepository.getOne(equipmentInfo.getId()),equipmentInfo);

        return equipmentInfoRepository.save(equipmentInfo);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    public EquipmentInfo findById(Integer id){
        if(equipmentInfoRepository.findById(id).isPresent())
            return equipmentInfoRepository.getOne(id);
        else
            throw new FkExceptions(EnumExceptions.SEARCH_FAILED_NOT_EXIST);
    }

    /**
     * 通过id批量删除
     * @param ids
     */
    @Transactional
    public void deleteInBatch(Integer[] ids){
        ActionLogUtil.log("设备信息",1,equipmentInfoRepository.findAllById(Arrays.asList(ids)));
        equipmentInfoRepository.deleteInBatch(equipmentInfoRepository.findAllById(Arrays.asList(ids)));
    }

    /**
     * 查询所有-分页
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<EquipmentInfo> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            EquipmentInfo.class.getDeclaredField(sortFieldName);
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
        return equipmentInfoRepository.findAll(pageable);
    }
}
