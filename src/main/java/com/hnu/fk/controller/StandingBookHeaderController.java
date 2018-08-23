package com.hnu.fk.controller;

import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.StandingBookDetail;
import com.hnu.fk.domain.StandingBookHeader;
import com.hnu.fk.service.StandingBookHeaderService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/standingBookHeader")
@Api(tags = "调度台账表头接口")
public class StandingBookHeaderController {
    @Autowired
    private StandingBookHeaderService standingBookHeaderService;
    /**
     * 新增
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增")
    public Result<StandingBookHeader> add(
            @ApiParam(value = "standingBookHeader") @RequestBody @Valid StandingBookHeader standingBookHeader){
        return ResultUtil.success(standingBookHeaderService.save(standingBookHeader));
    }
    /**
     * 编辑
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "编辑")
    public Result<StandingBookHeader> update(
            @ApiParam(value = "standingBookHeader") @RequestBody @Valid StandingBookHeader standingBookHeader){
        return ResultUtil.success(standingBookHeaderService.update(standingBookHeader));
    }
    /**
     * 通过日期和班次查询
     */
    @GetMapping(value = "/getByDateAndSchedule")
    @ApiOperation(value = "根据班次和日期范围查询")
    public Result<List<StandingBookHeader>> getByDateAndSchedule(
            @ApiParam(value = "开始日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @ApiParam(value = "班次,数据字典数据的主键") @RequestParam Long scheduleId ){
        return ResultUtil.success(standingBookHeaderService.findAllByScheduleAndDate(scheduleId,startDate,endDate));
    }
    /**
     * 通过日期和班次查询-分页
     */
    @GetMapping(value = "/getByDateAndScheduleByPage")
    @ApiOperation(value ="根据班次和日期范围查询-分页")
    public Result<Page<StandingBookHeader>> getByDateAndSchedule(
            @ApiParam(value = "开始日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @ApiParam(value = "班次,数据字典数据的主键") @RequestParam Long scheduleId,
            @ApiParam(value = "页码(从0开始)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc){
        return ResultUtil.success(standingBookHeaderService.findAllBySchedulAndDateByPage(scheduleId,startDate,endDate,page, size, sortFieldName, asc));
    }


}
