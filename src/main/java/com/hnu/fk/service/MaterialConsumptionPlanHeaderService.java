package com.hnu.fk.service;

import com.hnu.fk.domain.MaterialConsumptionPlanHeader;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.MaterialConsumptionPlanDetailRepository;
import com.hnu.fk.repository.MaterialConsumptionPlanHeaderRepository;
import com.hnu.fk.repository.UserRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 11:29 2018/8/22
 * @Modified By:
 */
@Service
public class MaterialConsumptionPlanHeaderService {
    
    private static final String NAME = "物料消耗计划表";
    
    @Autowired
    private MaterialConsumptionPlanHeaderRepository materialConsumptionPlanHeaderRepository;

    @Autowired
    private MaterialConsumptionPlanDetailRepository materialConsumptionPlanDetailRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 新增
     * 
     * @param materialConsumptionPlanHeader
     * @return
     */
    public MaterialConsumptionPlanHeader save(MaterialConsumptionPlanHeader materialConsumptionPlanHeader) {
        // 验证是否存在
        if (materialConsumptionPlanHeader == null || (materialConsumptionPlanHeader.getId() != null
                && materialConsumptionPlanHeaderRepository.findById(materialConsumptionPlanHeader.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        // 判断是否已录入
        if(materialConsumptionPlanHeaderRepository.findFirstByDate(materialConsumptionPlanHeader.getDate()) != null){
            throw new FkExceptions(EnumExceptions.DATA_ENTERED);
        }

        // 验证录入人是否存在
        if(materialConsumptionPlanHeader.getEnterUser() == null
                || userRepository.findById(materialConsumptionPlanHeader.getEnterUser().getId()).isPresent() == false){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_ENTER_USER_NOT_EXISTS);
        }

        // 设置录入时间
        materialConsumptionPlanHeader.setEnterTime(new Date());

        MaterialConsumptionPlanHeader save = materialConsumptionPlanHeaderRepository.save(materialConsumptionPlanHeader);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新
     * 
     * @param materialConsumptionPlanHeader
     * @return
     */
    @Transactional
    public MaterialConsumptionPlanHeader update(MaterialConsumptionPlanHeader materialConsumptionPlanHeader) {
        // 验证是否存在
        Optional<MaterialConsumptionPlanHeader> optional = null;
        if (materialConsumptionPlanHeader == null || materialConsumptionPlanHeader.getId() == null
                || (optional=materialConsumptionPlanHeaderRepository.findById(materialConsumptionPlanHeader.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        // 删除旧数据
        materialConsumptionPlanDetailRepository.deleteByHeader(optional.get());

        // 验证修改人是否存在
        if(materialConsumptionPlanHeader.getModifyUser() == null
                || userRepository.findById(materialConsumptionPlanHeader.getModifyUser().getId()).isPresent() == false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_MODIFY_USER_NOT_EXISTS);
        }

        // 修改时间
        materialConsumptionPlanHeader.setModifyTime(new Date());

        // 设置不可修改部分
        // 日期
        materialConsumptionPlanHeader.setDate(optional.get().getDate());

        // 录入人
        materialConsumptionPlanHeader.setEnterUser(optional.get().getEnterUser());

        // 录入时间
        materialConsumptionPlanHeader.setEnterTime(optional.get().getEnterTime());

        MaterialConsumptionPlanHeader oldHeader = optional.get();
        MaterialConsumptionPlanHeader newHeader = materialConsumptionPlanHeaderRepository.save(materialConsumptionPlanHeader);
        ActionLogUtil.log(NAME, oldHeader, newHeader);
        return newHeader;
    }

    /**
     * 通过日期查询
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<MaterialConsumptionPlanHeader> getByStartDateAndEndDate(Date startDate, Date endDate) {
        List<MaterialConsumptionPlanHeader> headers = materialConsumptionPlanHeaderRepository.findByDateBetween(startDate, endDate);
        for(MaterialConsumptionPlanHeader header : headers){
            if(header.getDetails() != null && header.getDetails().size() > 1){
                header.getDetails().sort((o1, o2)->{
                    if(o1.getItem() != null && o1.getItem().getId() != null && o2.getItem() != null && o2.getItem().getId() != null)
                        return o1.getItem().getId() - o2.getItem().getId();
                    return 0;
                });
            }
        }
        return headers;
    }
}
