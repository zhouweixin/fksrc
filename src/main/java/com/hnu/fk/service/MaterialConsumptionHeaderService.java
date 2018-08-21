package com.hnu.fk.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.hnu.fk.domain.ActionLog;
import com.hnu.fk.domain.MaterialConsumptionDetail;
import com.hnu.fk.domain.MaterialConsumptionHeader;
import com.hnu.fk.domain.MaterialConsumptionItem;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.MaterialConsumptionDetailRepository;
import com.hnu.fk.repository.MaterialConsumptionHeaderRepository;
import com.hnu.fk.repository.MaterialConsumptionItemRepository;
import com.hnu.fk.repository.UserRepository;
import com.hnu.fk.utils.ActionLogUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗表头
 * @Date: Created in 12:55 2018/8/21
 * @Modified By:
 */
@Service
public class MaterialConsumptionHeaderService {

    public static final String NAME = "物料消耗";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private MaterialConsumptionHeaderRepository materialConsumptionHeaderRepository;

    @Autowired
    private MaterialConsumptionDetailRepository materialConsumptionDetailRepository;

    @Autowired
    private MaterialConsumptionItemRepository materialConsumptionItemRepository;

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

        MaterialConsumptionHeader save = materialConsumptionHeaderRepository.save(materialConsumptionHeader);
        ActionLogUtil.log(NAME, 0, save);
        return save;
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

        MaterialConsumptionHeader oldHeader = optional.get();
        MaterialConsumptionHeader newHeader = materialConsumptionHeaderRepository.save(materialConsumptionHeader);
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
    public List<MaterialConsumptionHeader> getByStartDateAndEndDate(Date startDate, Date endDate) {
        return materialConsumptionHeaderRepository.findByDateBetween(startDate, endDate);
    }

    /**
     * 通过日期导出
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Workbook exportByStartDateAndEndDate(Date startDate, Date endDate) {
        // 写日志
        ActionLogUtil.log(NAME);

        // 查询表头
        List<MaterialConsumptionItem> items = materialConsumptionItemRepository.findAll();
        if (items == null || items.size() == 0) {
            throw new FkExceptions(EnumExceptions.EXPORT_FAILED_FIELD_NULL);
        }

        // 查询数据
        List<MaterialConsumptionHeader> headers = materialConsumptionHeaderRepository.findByDateBetween(startDate, endDate);
        if (headers == null || headers.size() == 0) {
            throw new FkExceptions(EnumExceptions.EXPORT_FAILED_DATA_NULL);
        }

        // 创建表
        List<ExcelExportEntity> entities = new ArrayList<>();

        // 【1、写表头】
        // 1.日期
        ExcelExportEntity entity = new ExcelExportEntity("日期", "date");
        entity.setFormat("yyyy-MM-dd");
        entities.add(new ExcelExportEntity("日期", "date"));

        // 2.实际用量
        entity = new ExcelExportEntity("实际用量(t)", "realConsuption");
        List<ExcelExportEntity> list = new ArrayList<>();
        for (MaterialConsumptionItem materialConsumptionItem : items) {
            list.add(new ExcelExportEntity(materialConsumptionItem.getName(), materialConsumptionItem.getId()));
        }
        entity.setList(list);
        entities.add(entity);

        // 3.录入时间
        entity = new ExcelExportEntity("录入时间", "enterTime");
        entity.setFormat("yyyy-MM-dd HH:mm:ss");
        entities.add(entity);

        // 4.录入人
        entities.add(new ExcelExportEntity("录入人", "enterUser"));

        // 5.修改时间
        entity = new ExcelExportEntity("修改时间", "modifyTime");
        entity.setFormat("yyyy-MM-dd HH:mm:ss");
        entities.add(entity);

        // 6.修改人
        entities.add(new ExcelExportEntity("修改人", "modifyUser"));

        // 【2、写数据】
        List<Map<Object, Object>> datas = new ArrayList<>();
        for (MaterialConsumptionHeader header : headers) {
            Map<Object, Object> map = new HashMap<>();

            if (header.getDate() != null) {
                map.put("date", header.getDate());
            }

            if (header.getEnterTime() != null) {
                map.put("enterTime", header.getEnterTime());
            }

            if (header.getModifyTime() != null) {
                map.put("modifyTime", header.getModifyTime());
            }

            if (header.getEnterUser() != null && header.getEnterUser().getName() != null) {
                map.put("enterUser", header.getEnterUser().getName());
            }

            if (header.getModifyUser() != null && header.getModifyUser().getName() != null) {
                map.put("modifyUser", header.getModifyUser().getName());
            }

            if(header.getMaterialConsumptionDetails() == null || header.getMaterialConsumptionDetails().size() == 0){
                continue;
            }

            Map<Object, Object> realConsuptionMap = new HashMap<>();
            for(MaterialConsumptionDetail detail : header.getMaterialConsumptionDetails()){
                if(detail.getItem() == null || detail.getItem().getId() == null || detail.getValue() == null){
                    continue;
                }

                realConsuptionMap.put(detail.getItem().getId(), detail.getValue());
            }

            map.put("realConsuption", Arrays.asList(realConsuptionMap));
            datas.add(map);
        }

        String title = "物料消耗表(" + sdf.format(startDate) + " " + sdf.format(endDate) + ")";
        return ExcelExportUtil.exportExcel(new ExportParams(title, "物料消耗表", ExcelType.XSSF), entities, datas);
    }
}
