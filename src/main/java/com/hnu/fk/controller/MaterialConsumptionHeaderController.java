package com.hnu.fk.controller;

import com.hnu.fk.domain.MaterialConsumptionHeader;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.MaterialConsumptionHeaderService;
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
import java.util.*;

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
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private MaterialConsumptionHeaderService materialConsumptionHeaderService;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/add")
    public Result<MaterialConsumptionHeader> add(
            @ApiParam(value = "materialConsumptionHeader") @RequestBody MaterialConsumptionHeader materialConsumptionHeader) {
        return ResultUtil.success(materialConsumptionHeaderService.save(materialConsumptionHeader));
    }

    @ApiOperation(value = "编辑")
    @PostMapping(value = "/update")
    public Result<MaterialConsumptionHeader> update(
            @ApiParam(value = "materialConsumptionHeader") @RequestBody MaterialConsumptionHeader materialConsumptionHeader) {
        return ResultUtil.success(materialConsumptionHeaderService.update(materialConsumptionHeader));
    }

    @ApiOperation(value = "通过日期查询")
    @GetMapping(value = "/getByStartDateAndEndDate")
    public Result<List<MaterialConsumptionHeader>> getByStartDateAndEndDate(
            @ApiParam(value = "开始日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResultUtil.success(materialConsumptionHeaderService.getByStartDateAndEndDate(startDate, endDate));
    }

    @ApiOperation(value = "通过日期查询-分页")
    @GetMapping(value = "/getByStartDateAndEndDateByPage")
    public Result<Page<MaterialConsumptionHeader>> getByStartDateAndEndDateByPage(
            @ApiParam(value = "开始日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        return ResultUtil.success(materialConsumptionHeaderService.getByStartDateAndEndDateByPage(startDate, endDate, page, size, sortFieldName, asc));
    }

    @ApiOperation(value = "通过日期导出excel")
    @GetMapping(value = "/exportByStartDateAndEndDate")
    public Result<Object> exportByStartDateAndEndDate(
            @ApiParam(value = "开始日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam(value = "结束日期, 格式为yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            HttpServletResponse response) throws IOException {

        Workbook workbook = materialConsumptionHeaderService.exportByStartDateAndEndDate(startDate, endDate);

        String fileName = "物料消耗表(" + sdf.format(startDate) + " " + sdf.format(endDate) + ")-" + new Date().getTime() + ".xlsx";
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        response.flushBuffer();
        workbook.write(response.getOutputStream());
        return ResultUtil.success();
    }
}
