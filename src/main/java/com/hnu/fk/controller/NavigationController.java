package com.hnu.fk.controller;

import com.hnu.fk.domain.FirstLevelMenu;
import com.hnu.fk.domain.Navigation;
import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.SecondLevelMenuOperation;
import com.hnu.fk.service.NavigationService;
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
 * @Description: 导航
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/navigation")
@Api(tags = "导航接口")
public class NavigationController {
    @Autowired
    private NavigationService navigationService;

    /**
     * 新增
     *
     * @param navigation
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<Navigation> add(@Valid Navigation navigation, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(navigationService.save(navigation));
    }

    /**
     * 更新名称
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/updateNameById")
    @ApiOperation(value = "更新名称")
    public Result<Navigation> updateNameById(@ApiParam(value = "主键") @RequestParam Integer id,
                                         @ApiParam(value = "名称") @RequestParam String name) {
        navigationService.updateNameById(id, name);
        return ResultUtil.success();
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
        navigationService.delete(id);
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
        navigationService.deleteByIdIn(ids);
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
    public Result<Navigation> getById(@ApiParam(value = "主键") @RequestParam Integer id) {
        return ResultUtil.success(navigationService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<Navigation>> getAll() {
        return ResultUtil.success(navigationService.findAll());

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
    public Result<Page<Navigation>> getAllByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(navigationService.findAllByPage(page, size, sortFieldName, asc));
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
    public Result<Page<Navigation>> getByNameLikeByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页码(从0开始)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(navigationService.findByNameLikeByPage(name, page, size, sortFieldName, asc));
    }

    /**
     * 查询所有可分配操作
     * @return
     */
    @GetMapping(value = "/getAllNavigationOperations")
    @ApiOperation(value = "查询所有可分配操作")
    public Result<List<Navigation>> findAllNavigationOperations(){
        return ResultUtil.success(navigationService.findAllNavigationOperations());
    }

    /**
     * 更新二级菜单可分配操作
     * @param MenuId
     * @param Operationids
     * @return
     */
    @PostMapping(value = "/updateNavigationOperations")
    @ApiOperation(value = "更新二级菜单可分配操作")
    public Result<List<SecondLevelMenuOperation>> updateSecondOperations(
            @ApiParam(value = "二级菜单主键") @RequestParam Integer MenuId,
            @ApiParam(value = "操作主键数组") @RequestParam Integer[] Operationids){
        return ResultUtil.success(navigationService.updateSecondLevelMenuOperatons(MenuId,Operationids));
    }

    /**
     * 通过主键查询一级菜单
     * @param id
     * @return
     */
    @GetMapping(value = "/getFirstLevelMenusById")
    @ApiOperation(value = "通过主键查询一级菜单")
    public Result<List<FirstLevelMenu>> getFirstLevelMenusById(@ApiParam(value = "导航主键") @RequestParam Integer id){
        return ResultUtil.success(navigationService.getFirstLevelMenusById(id));
    }

    @GetMapping(value = "/shift")
    @ApiOperation(value = "导航上下移动")
    public Result<Object> shift(
            @ApiParam(value = "导航1主键") @RequestParam Integer id1,
            @ApiParam(value = "导航2主键") @RequestParam Integer id2) {

        navigationService.shift(id1, id2);
        return ResultUtil.success();
    }
}
