package com.hnu.fk.controller;

import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.StandingBookItem;
import com.hnu.fk.service.StandingBookItemService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sun.awt.image.IntegerComponentRaster;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/standingBookItem")
@Api(tags = "调度台账项目接口")
public class StandingBookItemController {
    @Autowired
    private StandingBookItemService standingBookItemService;

    /**
     * 新增
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增")
    public Result<StandingBookItem> add(@Valid StandingBookItem standingBookItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        return ResultUtil.success(standingBookItemService.add(standingBookItem));
    }

    /**
     * 更新
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<StandingBookItem> update(@Valid StandingBookItem standingBookItem, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        return ResultUtil.success(standingBookItemService.update(standingBookItem));
    }
    /**
     * 删除
     */
    @DeleteMapping(value = "/deleteById")
    @ApiOperation(value = "根据id删除")
    public Result<Object> delete(@ApiParam(value = "主键") @RequestParam Integer id){
        standingBookItemService.delete(id);
        return ResultUtil.success();
    }
    /**
     * 批量删除
     */
    @DeleteMapping(value = "/deleteInBatch")
    @ApiOperation(value = "批量删除")
    public Result<Object> deleteInBatch(@ApiParam(value = "主键数组") @RequestParam Integer[] ids){
        standingBookItemService.deleteInBatch(ids);
        return ResultUtil.success();
    }
    /**
     * 根据id查询
     */
    @GetMapping(value = "/getById")
    @ApiOperation(value = "根据id查询")
    public Result<StandingBookItem> getById(@ApiParam(value = "主键") @RequestParam Integer id){
        return ResultUtil.success(standingBookItemService.findOne(id));
    }
    /**
     * 查询所有
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<StandingBookItem>> getAll(){
        return ResultUtil.success(standingBookItemService.findAll());
    }
    /**
     * 查询所有-分页
     */
    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "查询所有-分页")
    public Result<Page<StandingBookItem>> getAllByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(standingBookItemService.findAllByPage(page, size, sortFieldName, asc));
    }
    /**
     * 根据项目类型和名称模糊查询-分页
     */
    @GetMapping(value = "/getAllByNameLikeAndType")
    @ApiOperation(value = "根据项目类别和项目名称模糊查询")
    public Result<Page<StandingBookItem>> getByMaterialTypeAndNameLikeByPage(
            @ApiParam(value = "项目类型主键, 默认为-1, -1表示不启用", defaultValue = "-1") @RequestParam(value = "materialTypeId", defaultValue = "-1") Integer typeId,
            @ApiParam(value = "名称(默认为\"\")", defaultValue = "") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页码(从0开始)", defaultValue = "0") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)", defaultValue = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)", defaultValue = "id") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)", defaultValue = "1") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(standingBookItemService.findByNameLikeAndTypeByPage(page, size, sortFieldName, asc,name,typeId));
    }
}
