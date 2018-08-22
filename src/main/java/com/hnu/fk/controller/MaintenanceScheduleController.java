package com.hnu.fk.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.hnu.fk.domain.MaintenanceSchedule;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.MaintenanceScheduleService;
import com.hnu.fk.utils.ActionLogUtil;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
@RequestMapping(value = "/maintenanceSchedule")
@Api(tags = "检修计划")
public class MaintenanceScheduleController {
    @Autowired
    private MaintenanceScheduleService maintenanceScheduleService;


    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<MaintenanceSchedule> add(@Valid MaintenanceSchedule maintenanceSchedule, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultUtil.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return ResultUtil.success(maintenanceScheduleService.save(maintenanceSchedule));
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<MaintenanceSchedule> update(@Valid MaintenanceSchedule maintenanceSchedule, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return ResultUtil.success(maintenanceScheduleService.update(maintenanceSchedule));
    }

    @DeleteMapping(value = "/deleteByIds")
    @ApiOperation(value = "通过主键数组ids批量删除")
    public Result delete(@ApiParam(value = "主键数组ids") @RequestParam Integer[] ids){
        maintenanceScheduleService.deleteInBatch(ids);
        return ResultUtil.success();
    }

    @GetMapping(value = "/getById")
    @ApiOperation(value = "通过id查询")
    public Result<MaintenanceSchedule> getById(@ApiParam(value = "主键id") @RequestParam(value = "id") Integer id){
        return ResultUtil.success(maintenanceScheduleService.findById(id));
    }

    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "查询所有计划-分页")
    public Result<Page<MaintenanceSchedule>> getAllByPage(
            @ApiParam(value = "页码(默认为0)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "enteringTime") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；这里默认为0)") @RequestParam(value = "asc", defaultValue = "0") Integer asc) {

        return ResultUtil.success(maintenanceScheduleService.findAllByPage(page, size, sortFieldName, asc));
    }

    @GetMapping(value = "/getAllNotCompleteByPage")
    @ApiOperation(value = "查询所有未完成计划-分页")
    public Result<Page<MaintenanceSchedule>> getAllNotCompleteByPage(
            @ApiParam(value = "页码(默认为0)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为enteringTime)") @RequestParam(value = "sortFieldName", defaultValue = "enteringTime") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；这里默认为0)") @RequestParam(value = "asc", defaultValue = "0") Integer asc) {

        return ResultUtil.success(maintenanceScheduleService.findAllNotCompleteByPage(page, size, sortFieldName, asc));
    }

    @GetMapping(value = "getAllByNameAndDescriptionLikeByPage")
    @ApiOperation(value = "通过设备名称和故障描述模糊查询-分页")
    public Result<Page<MaintenanceSchedule>> getAllByNameAndDescriptionLikeByPage(
            @ApiParam(value = "设备名称") @RequestParam(value = "name") String name,
            @ApiParam(value = "故障描述") @RequestParam(value = "description",required = false) String description,
            @ApiParam(value = "页码(默认为0)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为enteringTime)") @RequestParam(value = "sortFieldName", defaultValue = "enteringTime") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；这里默认为0)") @RequestParam(value = "asc", defaultValue = "0") Integer asc) {

        return ResultUtil.success(maintenanceScheduleService.findAllByNameAndDescriptionLike(name,description,page, size, sortFieldName, asc));
    }

    @GetMapping(value = "/download")
    @ApiOperation(value = "导出excel",notes = "导出所有检修计划")
    public Result downloadThisYear(HttpServletResponse response) throws IOException {
        ActionLogUtil.log("检修计划");

        Sort sort = new Sort(Sort.Direction.DESC, "enteringTime");
        List<MaintenanceSchedule> maintenance = maintenanceScheduleService.findAll(sort);

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("检修计划表","sheet1",XSSF),
                MaintenanceSchedule.class,maintenance);

        Date time = new Date();
        String fileName = "检修计划表" + time.getTime() + ".xlsx";
        response.addHeader("Content-Disposition","attachment;fileName=" +new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        response.flushBuffer();
        workbook.write(response.getOutputStream());
        return ResultUtil.success();
    }
}
