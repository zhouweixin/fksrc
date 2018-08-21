package com.hnu.fk.controller;

import com.hnu.fk.domain.EquipmentInfo;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.EquipmentInfoService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
@RestController
@RequestMapping(value = "/equipmentInfo")
@Api(tags = "设备信息")
public class EquipmentInfoController {
    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参,")
    public Result<EquipmentInfo> add(@Valid EquipmentInfo equipmentInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultUtil.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return ResultUtil.success(equipmentInfoService.save(equipmentInfo));
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<EquipmentInfo> update(@Valid EquipmentInfo equipmentInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return ResultUtil.success(equipmentInfoService.update(equipmentInfo));
    }

    /**
     * 通过主键数组删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteByIds")
    @ApiOperation(value = "通过主键数组ids批量删除")
    public Result delete(@ApiParam(value = "主键数组ids") @RequestParam Integer[] ids){
        equipmentInfoService.deleteInBatch(ids);
        return ResultUtil.success();
    }

    /**
     * 查询所有-分页
     *
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "查询所有-分页")
    public Result<Page<EquipmentInfo>> getAllByPage(
            @ApiParam(value = "页码(默认为0)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；这里默认为0)") @RequestParam(value = "asc", defaultValue = "0") Integer asc) {

        return ResultUtil.success(equipmentInfoService.findAllByPage(page, size, sortFieldName, asc));
    }

}
