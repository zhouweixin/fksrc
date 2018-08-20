package com.hnu.fk.controller;

import com.hnu.fk.domain.MaterialConsumptionItem;
import com.hnu.fk.domain.MaterialType;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.MaterialConsumptionItemService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 17:41 2018/8/20
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/materialConsumptionItem")
@Api(tags = "物料消耗项目接口")
public class MaterialConsumptionItemController {
    @Autowired
    private MaterialConsumptionItemService materialConsumptionItemService;

    /**
     * 新增
     *
     * @param materialConsumptionItem
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<MaterialConsumptionItem> add(@Valid MaterialConsumptionItem materialConsumptionItem, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(materialConsumptionItemService.save(materialConsumptionItem));
    }

    /**
     * 更新
     *
     * @param materialConsumptionItem
     * @return
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<MaterialConsumptionItem> update(@Valid MaterialConsumptionItem materialConsumptionItem, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(materialConsumptionItemService.update(materialConsumptionItem));
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteById")
    @ApiOperation(value = "通过id删除")
    public Result<Object> deleteById(@ApiParam(value = "主键") @RequestParam Integer id) {
        materialConsumptionItemService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteByIds")
    @ApiOperation(value = "批量删除")
    public Result<Object> deleteByIds(@ApiParam(value = "主键数组") @RequestParam Integer[] ids) {
        materialConsumptionItemService.deleteByIdIn(ids);
        return ResultUtil.success();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getById")
    @ApiOperation(value = "通过id查询")
    public Result<MaterialConsumptionItem> getById(@ApiParam(value = "主键") @RequestParam Integer id) {
        return ResultUtil.success(materialConsumptionItemService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<MaterialConsumptionItem>> getAll() {
        return ResultUtil.success(materialConsumptionItemService.findAll());

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
    public Result<Page<MaterialConsumptionItem>> getAllByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(materialConsumptionItemService.findAllByPage(page, size, sortFieldName, asc));
    }

    /**
     * 通过物料类型和名称模糊查询-分页
     *
     * @param materialTypeId
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    @GetMapping(value = "/getByMaterialTypeAndNameLikeByPage")
    @ApiOperation(value = "通过物料类型和名称模糊查询-分页")
    public Result<Page<MaterialConsumptionItem>> getByMaterialTypeAndNameLikeByPage(
            @ApiParam(value = "物料类型主键, 默认为-1, -1表示不启用", defaultValue = "-1") @RequestParam(value = "materialTypeId", defaultValue = "-1") Integer materialTypeId,
            @ApiParam(value = "名称(默认为\"\")", defaultValue = "") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页码(从0开始)", defaultValue = "0") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)", defaultValue = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)", defaultValue = "id") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)", defaultValue = "1") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(materialConsumptionItemService.getByMaterialTypeAndNameLikeByPage(materialTypeId, name, page, size, sortFieldName, asc));
    }
}
