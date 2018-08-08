package com.hnu.fk.controller;

import com.hnu.fk.domain.*;
import com.hnu.fk.service.UserService;
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
 * @Date: Created in 11:30 2018/8/7
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/user")
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<User> add(@Valid User user) {
        return ResultUtil.success(userService.save(user));
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<User> update(@Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(userService.update(user));
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录")
    public Result<User> login(Integer id, String password) {
        return ResultUtil.success(userService.login(id, password));
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
        userService.delete(id);
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
        userService.deleteByIdIn(ids);
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
    public Result<User> getById(@ApiParam(value = "主键") @RequestParam Integer id) {
        return ResultUtil.success(userService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<User>> getAll() {
        return ResultUtil.success(userService.findAll());

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
    public Result<Page<User>> getAllByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(userService.findAllByPage(page, size, sortFieldName, asc));
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
    public Result<Page<User>> getByNameLikeByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页码(从0开始)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(userService.findByNameLikeByPage(name, page, size, sortFieldName, asc));
    }

    @GetMapping(value = "/resetPassword")
    @ApiOperation(value = "重置密码")
    public Result<Page<User>> resetPassword(
            @ApiParam(value = "用户主键") @RequestParam Integer id) {
        userService.resetPassword(id);
        return ResultUtil.success();
    }

    @PostMapping(value = "/updatePassword")
    @ApiOperation(value = "修改密码")
    public Result<Page<User>> updatePassword(
            @ApiParam(value = "用户主键") @RequestParam Integer id,
            @ApiParam(value = "旧密码") @RequestParam(value = "oldPassword") String oldPassword,
            @ApiParam(value = "新密码") @RequestParam(value = "newPassword") String newPassword,
            @ApiParam(value = "重复新密码") @RequestParam(value = "reNewPassword") String reNewPassword) {
        userService.updatePassword(id, oldPassword, newPassword, reNewPassword);
        return ResultUtil.success();
    }

    /**
     * 通过部门查询用户
     *
     * @param departmentId
     * @return
     */
    @GetMapping(value = "/getByDepartment")
    @ApiOperation(value = "通过部门查询用户")
    public Result<Page<User>> getByDepartment(
            @ApiParam(value = "部门") @RequestParam Integer departmentId,
            @ApiParam(value = "页码(从0开始)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {
        return ResultUtil.success(userService.findByDepartment(departmentId, page, size, sortFieldName, asc));
    }

    /**
     * 分配角色给用户
     * @param userIds
     * @param roleIds
     * @return
     */
    @PostMapping(value = "/assignRolesToUsers")
    @ApiOperation(value = "分配角色给用户")
    public Result<Object> assignRolesToUsers(
            @ApiParam(value = "用户主键数组") @RequestParam Integer[] userIds,
            @ApiParam(value = "权限主键数组") @RequestParam Integer[] roleIds){
        userService.assignRolesToUsers(userIds, roleIds);
        return ResultUtil.success();
    }

    @GetMapping(value = "/getRolesById")
    @ApiOperation(value = "查询用户的角色")
    public Result<List<Role>> getRolesById(Integer id){
        return ResultUtil.success(userService.getRolesById(id));
    }

    @GetMapping(value = "/getPermissionsById")
    @ApiOperation(value = "查询用户的权限")
    public Result<List<RoleSecondLevelMenuOperation>> getPermissionsById(Integer id){
        return ResultUtil.success(userService.getPermissionsById(id));
    }
}
