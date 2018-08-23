package com.hnu.fk.controller;

import com.hnu.fk.domain.MaterialConsumptionReportHeader;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.MaterialConsumptionReportHeaderService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private MaterialConsumptionReportHeaderService materialConsumptionReportHeaderService;

    @ApiOperation(value = "生成报表")
    @GetMapping(value = "/generateReport")
    public Result<MaterialConsumptionReportHeader> generateReport(
            @ApiParam(value = "用户主键") @RequestParam @DateTimeFormat(pattern = "yyyy-MM") Integer enterUserId,
            @ApiParam(value = "日期: 格式yyyy-MM") @RequestParam @DateTimeFormat(pattern = "yyyy-MM") Date date) {
        materialConsumptionReportHeaderService.generateReport(enterUserId, date);
        return ResultUtil.success();
    }

    @ApiOperation(value = "重新生成报表")
    @GetMapping(value = "/reGenerateReport")
    public Result<MaterialConsumptionReportHeader> reGenerateReport(
            @ApiParam(value = "用户主键") @RequestParam @DateTimeFormat(pattern = "yyyy-MM") Integer enterUserId,
            @ApiParam(value = "日期: 格式yyyy-MM") @RequestParam @DateTimeFormat(pattern = "yyyy-MM") Date date) {
        materialConsumptionReportHeaderService.reGenerateReport(enterUserId, date);
        return ResultUtil.success();
    }

    @ApiOperation(value = "通过日期查询-分页")
    @GetMapping(value = "/getByStartDateAndEndDateByPage")
    public Result<Page<MaterialConsumptionReportHeader>> getByStartDateAndEndDateByPage(
            @ApiParam(value = "开始日期, 格式为yyyy-MM") @RequestParam @DateTimeFormat(pattern = "yyyy-MM") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM") @RequestParam @DateTimeFormat(pattern = "yyyy-MM") Date endDate,
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为date)") @RequestParam(value = "sortFieldName", defaultValue = "date") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        return ResultUtil.success(materialConsumptionReportHeaderService.getByStartDateAndEndDateByPage(startDate, endDate, page, size, sortFieldName, asc));
    }

    @ApiOperation(value = "通过日期导出excel")
    @GetMapping(value = "/exportByStartDateAndEndDate")
    public Result<Object> exportByStartDateAndEndDate(
            @ApiParam(value = "开始日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            HttpServletResponse response) throws IOException {

        Workbook workbook = materialConsumptionReportHeaderService.exportByStartDateAndEndDate(startDate, endDate);

        String fileName = "物料消耗统计报表(" + sdf.format(startDate) + " " + sdf.format(endDate) + ")-" + new Date().getTime() + ".xlsx";
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        response.flushBuffer();
        workbook.write(response.getOutputStream());
        return ResultUtil.success();
    }

}
