package com.hnu.fk.service;

import com.hnu.fk.domain.*;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.MaterialConsumptionHeaderRepository;
import com.hnu.fk.repository.MaterialConsumptionPlanHeaderRepository;
import com.hnu.fk.repository.MaterialConsumptionReportHeaderRepository;
import com.hnu.fk.repository.TemporalIntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 生成报表
     *
     * @param date
     * @return
     */
    public void generateReport(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 判断是否已生成
        if (materialConsumptionReportHeaderRepository.findFirstByDate(date) != null) {
            throw new FkExceptions(EnumExceptions.REPORT_GENERATED);
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

        // 2.查询当前月每日数据
        List<MaterialConsumptionHeader> curDayHeaders = materialConsumptionHeaderRepository.findByDateBetween(curTemporalInterval.getStartDate(), curTemporalInterval.getEndDate());
        // 查询累计每日数据
        List<MaterialConsumptionHeader> sumDayHeaders = materialConsumptionHeaderRepository.findByDateBetween(sumTemporalInterval.getStartDate(), curTemporalInterval.getEndDate());

        // 按类型汇总数据
        Map<Integer, Double> curDayMap = new HashMap<>();
        Map<Integer, Double> sumDayMap = new HashMap<>();

        for (MaterialConsumptionHeader curDayHeader : curDayHeaders) {
            List<MaterialConsumptionDetail> curDayDetails = curDayHeader.getMaterialConsumptionDetails();
            if (curDayDetails != null && curDayDetails.size() > 0) {
                for (MaterialConsumptionDetail curDayDetail : curDayDetails) {
                    curDayMap.put(curDayDetail.getItem().getId(), curDayMap.getOrDefault(curDayDetail.getItem().getId(), 0.0) + curDayDetail.getValue());
                }
            }
        }

        for (MaterialConsumptionHeader sumDayHeader : sumDayHeaders) {
            List<MaterialConsumptionDetail> curDayDetails = sumDayHeader.getMaterialConsumptionDetails();
            if (curDayDetails != null && curDayDetails.size() > 0) {
                for (MaterialConsumptionDetail curDayDetail : curDayDetails) {
                    curDayMap.put(curDayDetail.getItem().getId(), curDayMap.getOrDefault(curDayDetail.getItem().getId(), 0.0) + curDayDetail.getValue());
                }
            }
        }

        // 3.创建报表
        MaterialConsumptionReportHeader header = new MaterialConsumptionReportHeader();
        List<MaterialConsumptionReportDetail> details = new ArrayList<>();
        header.setDetails(details);
        for (MaterialConsumptionPlanDetail materialConsumptionPlanDetail : planHeader.getDetails()) {
            // 4.创建报表数据: 单价, 计划单耗, 库存量
            MaterialConsumptionReportDetail detail = new MaterialConsumptionReportDetail(materialConsumptionPlanDetail.getPrice(), materialConsumptionPlanDetail.getPlanUnitConsumption(), materialConsumptionPlanDetail.getStorage());

//            detail.set

            details.add(detail);
        }
    }

//    private statistic
}
