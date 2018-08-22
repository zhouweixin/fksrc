package com.hnu.fk.controller;

import com.hnu.fk.domain.MaterialConsumptionReportHeader;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.MaterialConsumptionReportHeaderService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗统计表头
 * @Date: Created in 13:30 2018/8/22
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/materialConsumptionReportHeader")
@Api(tags = "物料消耗统计表头接口")
public class MaterialConsumptionReportHeaderController {
    @Autowired
    private MaterialConsumptionReportHeaderService materialConsumptionReportHeaderService;

    @ApiOperation(value = "生成报表")
    @GetMapping(value = "/generateReport")
    public Result<MaterialConsumptionReportHeader> generateReport(
            @ApiParam(value = "date") @RequestParam @DateTimeFormat(pattern = "yyyy-MM") Date date) {
        materialConsumptionReportHeaderService.generateReport(date);
        return ResultUtil.success();
    }
}
