package com.hnu.fk.controller;

import com.hnu.fk.domain.MaterialConsumptionPlanHeader;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.MaterialConsumptionPlanHeaderService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗计划表头
 * @Date: Created in 11:30 2018/8/22
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/materialConsumptionPlanHeader")
@Api(tags = "物料消耗计划表头接口")
public class MaterialConsumptionPlanHeaderController {
    @Autowired
    private MaterialConsumptionPlanHeaderService materialConsumptionPlanHeaderService;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/add")
    public Result<MaterialConsumptionPlanHeader> add(
            @ApiParam(value = "materialConsumptionPlanHeader") @RequestBody MaterialConsumptionPlanHeader materialConsumptionPlanHeader) {
        return ResultUtil.success(materialConsumptionPlanHeaderService.save(materialConsumptionPlanHeader));
    }

    @ApiOperation(value = "编辑")
    @PostMapping(value = "/update")
    public Result<MaterialConsumptionPlanHeader> update(
            @ApiParam(value = "materialConsumptionHeader") @RequestBody MaterialConsumptionPlanHeader materialConsumptionPlanHeader) {
        return ResultUtil.success(materialConsumptionPlanHeaderService.update(materialConsumptionPlanHeader));
    }

    @ApiOperation(value = "通过日期查询")
    @GetMapping(value = "/getByStartDateAndEndDate")
    public Result<List<MaterialConsumptionPlanHeader>> getByStartDateAndEndDate(
            @ApiParam(value = "开始日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResultUtil.success(materialConsumptionPlanHeaderService.getByStartDateAndEndDate(startDate, endDate));
    }
}
