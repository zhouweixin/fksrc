package com.hnu.fk.controller;

import com.hnu.fk.domain.Department;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.DepartmentService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description: 部门
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/department")
@Api(tags = "部门接口")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    /**
     * 新增
     *
     * @param department
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<Department> add(@Valid Department department, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(departmentService.save(department));
    }

    /**
     * 更新
     *
     * @param department
     * @return
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<Department> update(@Valid Department department, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(departmentService.update(department));
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteById")
    public Result<Object> deleteById(Integer id) {
        departmentService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteByIds")
    public Result<Object> deleteByIds(Integer[] ids) {
        departmentService.deleteByIdIn(ids);
        return ResultUtil.success();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getById")
    public Result<Department> getById(Integer id) {
        return ResultUtil.success(departmentService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    public Result<List<Department>> getAll() {
        return ResultUtil.success(departmentService.findAll());

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
    public Result<Page<Department>> getAllByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                             @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
                                             @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(departmentService.findAllByPage(page, size, sortFieldName, asc));
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
    public Result<Page<Department>> getByNameLikeByPage(@RequestParam(value = "name", defaultValue = "") String name,
                                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                    @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
                                                    @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(departmentService.findByNameLikeByPage(name, page, size, sortFieldName, asc));
    }
}
