package com.hnu.fk.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.TemporalInterval;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.service.TemporalIntervalService;
import com.hnu.fk.utils.ActionLogUtil;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cn.afterturn.easypoi.excel.entity.enmus.ExcelType.XSSF;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/13
 */
@RestController
@RequestMapping(value = "/temporalInterval")
@Api(tags = "月统计时间区间表")
public class TemporalIntervalController {
    @Autowired
    private TemporalIntervalService temporalIntervalService;

    /**
     * 查询某一年
     * @return
     */
    @GetMapping(value = "getAllByYear")
    @ApiOperation(value = "按照年份查询",notes = "输出当年全部12个月数据")
    public Result<List<TemporalInterval>> getAllByYear(@ApiParam(value = "年份") @RequestParam String statisticalYear){
        return ResultUtil.success(temporalIntervalService.getAllByYear(statisticalYear));
    }

    /**
     * 编辑，传入主键存在则更新，不存在则新增
     * @param temporalInterval
     * @return
     */
    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑",notes = "所编辑月份无数据时，序号输入-1")
    public Result<TemporalInterval> update(TemporalInterval temporalInterval){
        return ResultUtil.success(temporalIntervalService.edit(temporalInterval));
    }

    /**
     * 导出excel
     * @param statisticalYear
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/download")
    @ApiOperation(value = "导出excel",notes = "点击下载指定年份的excel")
    public Result downloadThisYear(@ApiParam(value = "年份") @RequestParam String statisticalYear, HttpServletResponse response) throws IOException {
        ActionLogUtil.log("月统计时间区间表");

        List<TemporalInterval> temporalIntervals = temporalIntervalService.getAllByYear(statisticalYear);

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("月统计时间区间表",statisticalYear,XSSF),
                TemporalInterval.class,temporalIntervals);

        response.flushBuffer();
        workbook.write(response.getOutputStream());

        return ResultUtil.success();
    }
}
