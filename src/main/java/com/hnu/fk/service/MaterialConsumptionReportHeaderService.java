package com.hnu.fk.service;

import com.hnu.fk.domain.*;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 13:28 2018/8/22
 * @Modified By:
 */
@Service
public class MaterialConsumptionReportHeaderService {

    public static final String NAME = "物料消耗统计表";

    @Autowired
    private MaterialConsumptionReportHeaderRepository materialConsumptionReportHeaderRepository;

    @Autowired
    private MaterialConsumptionPlanHeaderRepository materialConsumptionPlanHeaderRepository;

    @Autowired
    private MaterialConsumptionHeaderRepository materialConsumptionHeaderRepository;

    @Autowired
    private TemporalIntervalRepository temporalIntervalRepository;

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 生成报表
     *
     * @param date
     * @return
     */
    public void generateReport(Integer enterUserId, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 判断是否已生成
        if (materialConsumptionReportHeaderRepository.findFirstByDate(date) != null) {
            throw new FkExceptions(EnumExceptions.REPORT_GENERATED);
        }

        // 验证录入人是否存在
        Optional<User> optional = userRepository.findById(enterUserId);
        if(optional.isPresent() == false){
            throw new FkExceptions(EnumExceptions.REPORT_GENERATED_FAILED_ENTER_USER_NOT_EXISTS);
        }

        // 1.查询月计划数据
        MaterialConsumptionPlanHeader planHeader = materialConsumptionPlanHeaderRepository.findFirstByDate(date);
        if (planHeader == null) {
            throw new FkExceptions(EnumExceptions.REPORT_GENERATED_FAILED_PLAN_DATA_NOT_ENTERED);
        }

        // 判断月计划数据
        if (planHeader.getDetails() == null && planHeader.getDetails().size() == 0) {
            throw new FkExceptions(EnumExceptions.REPORT_GENERATED_FAILED_PLAN_DATA_NULL);
        }

        // 查询起始日期和终止日期
        TemporalInterval curTemporalInterval = temporalIntervalRepository.findFirstByStatisticalYearAndStatisticalMonth(calendar.get(Calendar.YEAR) + "",
                calendar.get(Calendar.MONTH) + "");
        TemporalInterval sumTemporalInterval = temporalIntervalRepository.findFirstByStatisticalYearAndStatisticalMonth(calendar.get(Calendar.YEAR) + "",
                "1");

        if(curTemporalInterval == null || sumTemporalInterval == null){
            throw new FkExceptions(EnumExceptions.REPORT_GENERATED_FAILED_MONTH_NOT_EXISTS);
        }

        // 2.查询当前月每日数据
        List<MaterialConsumptionHeader> curDayHeaders = materialConsumptionHeaderRepository.findByDateBetween(curTemporalInterval.getStartDate(), curTemporalInterval.getEndDate());
        // 查询累计每日数据
        List<MaterialConsumptionHeader> sumDayHeaders = materialConsumptionHeaderRepository.findByDateBetween(sumTemporalInterval.getStartDate(), curTemporalInterval.getEndDate());

        // 按类型汇总数据
        Map<Integer, Double> curDayMap = statisticConsuption(curDayHeaders);
        Map<Integer, Double> sumDayMap = statisticConsuption(sumDayHeaders);

        // 查询所有类型
        List<MaterialType> materialTypes = materialTypeRepository.findAll();
        Map<Integer, MaterialConsumptionReportTypeDetail> materialTypeId2TypeDetail = new HashMap<>();
        for(MaterialType materialType : materialTypes){
            materialTypeId2TypeDetail.put(materialType.getId(), new MaterialConsumptionReportTypeDetail(materialType));
        }

        // 3.创建报表
        MaterialConsumptionReportHeader header = new MaterialConsumptionReportHeader(optional.get(), date, curTemporalInterval.getStartDate(), curTemporalInterval.getEndDate());

        // 4.设置表头数据
        // TODO 获取: 本月实际原矿处理量
        header.setCurRawOre(2.0);
        // TODO 获取: 本月累计原矿处理量
        header.setTotalRawOre(2.0);

        // 5.设置详细数据
        Set<MaterialConsumptionReportDetail> details = new HashSet<>();
        header.setDetails(details);
        for (MaterialConsumptionPlanDetail planDetail : planHeader.getDetails()) {
            // 4.创建报表数据: 单价, 计划单耗, 库存量
            MaterialConsumptionReportDetail detail = new MaterialConsumptionReportDetail(planDetail.getPrice(), planDetail.getPlanUnitConsumption(), planDetail.getStorage());

            // 物料消耗项目
            detail.setItem(planDetail.getItem());

            // 本月实际用量, 本月实际单耗
            detail.setCurrentMonthAndCurConsump(curDayMap.getOrDefault(planDetail.getItem().getId(), 0.0), header.getCurRawOre());

            // 本月累计用量, 本月累计单耗
            detail.setTotalMonth(sumDayMap.getOrDefault(planDetail.getItem().getId(), 0.0), header.getTotalRawOre());

            // 计算本月成本
            int key = detail.getItem().getMaterialType().getId();
            double curCost = detail.getPrice() * detail.getCurConsump();
            double totalCost = detail.getPrice() * detail.getTotalConsump();
            if(materialTypeId2TypeDetail.containsKey(key)){
                MaterialConsumptionReportTypeDetail typeDetail = materialTypeId2TypeDetail.get(key);
                // 设置本月成本, 本月单位成本
                typeDetail.setCurCostAndCurUnitCost(typeDetail.getCurCost() + curCost, header.getCurRawOre());
                // 设置累计成本, 累计单位成本
                typeDetail.setTotalCostAndTotalUnitCost(typeDetail.getTotalCost() + totalCost, header.getTotalRawOre());
            } else {
                MaterialConsumptionReportTypeDetail typeDetail = new MaterialConsumptionReportTypeDetail(detail.getItem().getMaterialType());
                // 设置本月成本, 本月单位成本
                typeDetail.setCurCostAndCurUnitCost(typeDetail.getCurCost() + curCost, header.getCurRawOre());
                // 设置累计成本, 累计单位成本
                typeDetail.setTotalCostAndTotalUnitCost(typeDetail.getTotalCost() + totalCost, header.getTotalRawOre());
                materialTypeId2TypeDetail.put(key, typeDetail);
            }

            details.add(detail);
        }

        // 6.设置类型详细数据
        header.setTypeDetails(new HashSet<>(materialTypeId2TypeDetail.values()));

        materialConsumptionReportHeaderRepository.save(header);
    }

    /**
     * 重新生成报表
     *
     * @param enterUserId
     * @param date
     */
    @Transactional
    public void reGenerateReport(Integer enterUserId, Date date) {
        // 1.删除
        materialConsumptionReportHeaderRepository.deleteByDate(date);

        // 2.生成
        this.generateReport(enterUserId, date);
    }

    /**
     * 通过日期查询-分页
     *
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<MaterialConsumptionReportHeader> getByStartDateAndEndDateByPage(Date startDate, Date endDate, Integer page, Integer size, String sortFieldName, Integer asc) {
        // 判断排序字段名是否存在
        try {
            MaterialConsumptionHeader.class.getDeclaredField(sortFieldName);
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
        return materialConsumptionReportHeaderRepository.findByDateBetween(startDate, endDate, pageable);
    }

    /**
     * 按类别汇总消耗量
     *
     * @param headers
     * @return
     */
    private Map<Integer, Double> statisticConsuption(List<MaterialConsumptionHeader> headers){
        Map<Integer, Double> map = new HashMap<>();

        for (MaterialConsumptionHeader header : headers) {
            List<MaterialConsumptionDetail> details = header.getMaterialConsumptionDetails();
            if (details != null && details.size() > 0) {
                for (MaterialConsumptionDetail detail : details) {
                    map.put(detail.getItem().getId(), map.getOrDefault(detail.getItem().getId(), 0.0) + detail.getValue());
                }
            }
        }

        return map;
    }

    /**
     * 通过日期导出报表
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Workbook exportByStartDateAndEndDate(Date startDate, Date endDate) {
        return null;
    }
}
