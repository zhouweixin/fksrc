package com.hnu.fk.service;

import com.hnu.fk.domain.MaterialConsumptionDetail;
import com.hnu.fk.domain.MaterialConsumptionHeader;
import com.hnu.fk.domain.UserRole;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.MaterialConsumptionDetailRepository;
import com.hnu.fk.repository.MaterialConsumptionHeaderRepository;
import com.hnu.fk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗表头
 * @Date: Created in 12:55 2018/8/21
 * @Modified By:
 */
@Service
public class MaterialConsumptionHeaderService {

    public static final String NAME = "物料消耗";

    @Autowired
    private MaterialConsumptionHeaderRepository materialConsumptionHeaderRepository;

    @Autowired
    private MaterialConsumptionDetailRepository materialConsumptionDetailRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 新增
     *
     * @param materialConsumptionHeader
     * @return
     */
    public MaterialConsumptionHeader save(MaterialConsumptionHeader materialConsumptionHeader) {

        // 验证是否存在
        if (materialConsumptionHeader == null || (materialConsumptionHeader.getId() != null
                && materialConsumptionHeaderRepository.findById(materialConsumptionHeader.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        // 验证录入人是否存在
        if(materialConsumptionHeader.getEnterUser() == null
                || userRepository.findById(materialConsumptionHeader.getEnterUser().getId()).isPresent() == false){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_ENTER_USER_NOT_EXISTS);
        }

        // 设置录入时间
        materialConsumptionHeader.setEnterTime(new Date());

        return materialConsumptionHeaderRepository.save(materialConsumptionHeader);
    }

    /**
     * 编辑
     *
     * @param materialConsumptionHeader
     * @return
     */
    @Transactional
    public MaterialConsumptionHeader update(MaterialConsumptionHeader materialConsumptionHeader) {

        // 验证是否存在
        Optional<MaterialConsumptionHeader> optional = null;
        if (materialConsumptionHeader == null || materialConsumptionHeader.getId() == null
                || (optional=materialConsumptionHeaderRepository.findById(materialConsumptionHeader.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        // 删除旧数据
        materialConsumptionDetailRepository.deleteByHeader(optional.get());

        // 验证录入人是否存在
        if(materialConsumptionHeader.getModifyUser() == null
                || userRepository.findById(materialConsumptionHeader.getModifyUser().getId()).isPresent() == false){
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_MODIFY_USER_NOT_EXISTS);
        }

        // 修改时间
        materialConsumptionHeader.setModifyTime(new Date());

        // 设置不可修改部分
        // 日期
        materialConsumptionHeader.setDate(optional.get().getDate());

        // 录入人
        materialConsumptionHeader.setEnterUser(optional.get().getEnterUser());

        // 录入时间
        materialConsumptionHeader.setEnterTime(optional.get().getEnterTime());

        return materialConsumptionHeaderRepository.save(materialConsumptionHeader);
    }

    /**
     * 通过日期查询
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<MaterialConsumptionHeader> getByStartDateAndEndDate(Date startDate, Date endDate) {
        return materialConsumptionHeaderRepository.findByDateBetween(startDate, endDate);
    }
}
