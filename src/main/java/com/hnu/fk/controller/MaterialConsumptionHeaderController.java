package com.hnu.fk.controller;

import com.hnu.fk.domain.MaterialConsumptionHeader;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.MaterialConsumptionHeaderService;
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
 * @Description: 物料消耗表头
 * @Date: Created in 12:55 2018/8/21
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/materialConsumptionHeader")
@Api(tags = "物料消耗表头接口")
public class MaterialConsumptionHeaderController {
    @Autowired
    private MaterialConsumptionHeaderService materialConsumptionHeaderService;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/add")
    public Result<MaterialConsumptionHeader> add(
            @ApiParam(value = "materialConsumptionHeader") @RequestBody MaterialConsumptionHeader materialConsumptionHeader){
        return ResultUtil.success(materialConsumptionHeaderService.save(materialConsumptionHeader));
    }

    @ApiOperation(value = "编辑")
    @PostMapping(value = "/update")
    public Result<MaterialConsumptionHeader> update(
            @ApiParam(value = "materialConsumptionHeader") @RequestBody MaterialConsumptionHeader materialConsumptionHeader){
        return ResultUtil.success(materialConsumptionHeaderService.update(materialConsumptionHeader));
    }

    @ApiOperation(value = "通过日期查询")
    @PostMapping(value = "/getByStartDateAndEndDate")
    public Result<List<MaterialConsumptionHeader>> getByStartDateAndEndDate(
            @ApiParam(value = "开始日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return ResultUtil.success(materialConsumptionHeaderService.getByStartDateAndEndDate(startDate, endDate));
    }
}
