package com.hnu.fk.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.hnu.fk.domain.BackFillInfo;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.BackFillInfoService;
import com.hnu.fk.utils.ActionLogUtil;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static cn.afterturn.easypoi.excel.entity.enmus.ExcelType.XSSF;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
@RestController
@RequestMapping(value = "/backFillInfo")
@Api(tags = "回填工单")
public class BackFillInfoController {
    @Autowired
    private BackFillInfoService backFillInfoService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<BackFillInfo> add(@Valid BackFillInfo backFillInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultUtil.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return ResultUtil.success(backFillInfoService.save(backFillInfo));
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<BackFillInfo> update(@Valid BackFillInfo backFillInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return ResultUtil.success(backFillInfoService.update(backFillInfo));
    }

    @DeleteMapping(value = "/deleteByIds")
    @ApiOperation(value = "通过主键数组ids批量删除")
    public Result delete(@ApiParam(value = "主键数组ids") @RequestParam Integer[] ids){
        backFillInfoService.deleteInBatch(ids);
        return ResultUtil.success();
    }

    @GetMapping(value = "/getById")
    @ApiOperation(value = "通过id查询")
    public Result<BackFillInfo> getById(@ApiParam(value = "主键id") @RequestParam Integer id){
        return ResultUtil.success(backFillInfoService.findById(id));
    }

    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "查询所有记录-分页")
    public Result<Page<BackFillInfo>> getAllByPage(
            @ApiParam(value = "页码(默认为0)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "enteringTime") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；这里默认为0)") @RequestParam(value = "asc", defaultValue = "0") Integer asc) {

        return ResultUtil.success(backFillInfoService.findAllByPage(page, size, sortFieldName, asc));
    }

    @GetMapping(value = "/getAllByEquipmentAndTime")
    @ApiOperation(value = "通过设备和报修时间范围查询")
    public Result<Page<BackFillInfo>> getAllByEquipmentAndTime(
            @ApiParam(value = "开始日期，格式为yyyy-MM-dd") @RequestParam(value = "startDate") String startDate,
            @ApiParam(value = "结束日期，格式为yyyy-MM-dd") @RequestParam(value = "endDate") String endDate,
            @ApiParam(value = "设备，不传值时查询所有设备") @RequestParam(value = "equipmentCode",defaultValue = "") String equipmentCode,
            @ApiParam(value = "页码(默认为0)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "enteringTime") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；这里默认为0)") @RequestParam(value = "asc", defaultValue = "0") Integer asc) {

        return ResultUtil.success(backFillInfoService.getAllByEquipmentAndTime(startDate,endDate,equipmentCode,page, size, sortFieldName, asc));
    }

    @GetMapping(value = "/getByDateToExcel")
    @ApiOperation(value = "通过时间段导出")
    public Result getByDateToExcel(
            @ApiParam(value = "开始日期，格式为yyyy-MM-dd") @RequestParam(value = "startDate") String startDate,
            @ApiParam(value = "结束日期，格式为yyyy-MM-dd") @RequestParam(value = "endDate") String endDate,
            @ApiParam(value = "设备，不传值时查询所有设备") @RequestParam(value = "equipmentCode",defaultValue = "") String equipmentCode,
            HttpServletResponse response) throws IOException {

        ActionLogUtil.log("回填工单");

        List<BackFillInfo> backFillInfos = backFillInfoService.getAllByEquipmentAndTime(startDate,endDate,equipmentCode);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("回填工单","sheet1" ,XSSF),
                BackFillInfo.class,backFillInfos);

        Date time = new Date();
        String fileName = "回填工单" + time.getTime() + ".xlsx";
        response.addHeader("Content-Disposition","attachment;fileName=" +new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        response.flushBuffer();
        workbook.write(response.getOutputStream());

        return ResultUtil.success();
    }
}
