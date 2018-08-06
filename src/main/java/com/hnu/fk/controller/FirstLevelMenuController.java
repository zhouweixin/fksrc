package com.hnu.fk.controller;

import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.FirstLevelMenu;
import com.hnu.fk.service.FirstLevelMenuService;
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
 * @Description: 一级菜单接口
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/firstLevelMenu")
@Api(tags = "一级菜单接口")
public class FirstLevelMenuController {
    @Autowired
    private FirstLevelMenuService firstLevelMenuService;

    /**
     * 新增
     *
     * @param firstLevelMenu
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<FirstLevelMenu> add(@Valid FirstLevelMenu firstLevelMenu, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(firstLevelMenuService.save(firstLevelMenu));
    }

    /**
     * 更新
     *
     * @param firstLevelMenu
     * @return
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<FirstLevelMenu> update(@Valid FirstLevelMenu firstLevelMenu, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(firstLevelMenuService.update(firstLevelMenu));
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
        firstLevelMenuService.delete(id);
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
        firstLevelMenuService.deleteByIdIn(ids);
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
    public Result<FirstLevelMenu> getById(@ApiParam(value = "主键") @RequestParam Integer id) {
        return ResultUtil.success(firstLevelMenuService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<FirstLevelMenu>> getAll() {
        return ResultUtil.success(firstLevelMenuService.findAll());

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
    public Result<Page<FirstLevelMenu>> getAllByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(firstLevelMenuService.findAllByPage(page, size, sortFieldName, asc));
    }

    /**
     * 通过名称模糊查询-分页
     *
     * @param name
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    @GetMapping(value = "/getByNameLikeByPage")
    @ApiOperation(value = "通过名称模糊查询-分页")
    public Result<Page<FirstLevelMenu>> getByNameLikeByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页码(从0开始)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(firstLevelMenuService.findByNameLikeByPage(name, page, size, sortFieldName, asc));
    }
}
