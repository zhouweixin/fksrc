package com.hnu.fk.controller;

import com.hnu.fk.domain.Navigation;
import com.hnu.fk.domain.Role;
import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.RoleSecondLevelMenuOperation;
import com.hnu.fk.service.RoleService;
import com.hnu.fk.utils.ResultUtil;
import com.hnu.fk.vo.RoleAssignUsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description: 角色接口
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/role")
@Api(tags = "角色接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 新增
     *
     * @param role
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增", notes = "id自增长不需要传参")
    public Result<Role> add(@Valid Role role, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(roleService.save(role));
    }

    /**
     * 更新
     *
     * @param role
     * @return
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<Role> update(@Valid Role role, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(roleService.update(role));
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
        roleService.delete(id);
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
        roleService.deleteByIdIn(ids);
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
    public Result<Role> getById(@ApiParam(value = "主键") @RequestParam Integer id) {
        return ResultUtil.success(roleService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<Role>> getAll() {
        return ResultUtil.success(roleService.findAll());

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
    public Result<Page<Role>> getAllByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(roleService.findAllByPage(page, size, sortFieldName, asc));
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
    public Result<Page<Role>> getByNameLikeByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页码(从0开始)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(roleService.findByNameLikeByPage(name, page, size, sortFieldName, asc));
    }

    @GetMapping(value = "/getPermissionsById")
    @ApiOperation(value = "查询角色分配的权限")
    public Result<List<Navigation>> getPermissionsById(
            @ApiParam(value = "角色主键") @RequestParam int id){
        return ResultUtil.success(roleService.getPermissionsById(id));
    }

    @PostMapping(value = "/assignPermissions")
    @ApiOperation(value = "分配权限给角色")
    public Result<Object> assignPermissions(
            @ApiParam(value = "角色菜单操作对应关系") @RequestBody Set<RoleSecondLevelMenuOperation> permissions){
        roleService.assignPermissions(permissions);
        return ResultUtil.success();
    }

    @GetMapping(value = "/getAssignUsersById")
    @ApiOperation(value = "查询角色分配的用户")
    public Result<RoleAssignUsersVO> getAssignUsersById(
            @ApiParam(value = "角色主键") @RequestParam int id){
        return ResultUtil.success(roleService.getAssignUsersById(id));
    }

    @PostMapping(value = "/assignRoleToUsers")
    @ApiOperation(value = "分配角色给用户")
    public Result<Object> assignRoleToUsers(
            @ApiParam(value = "角色主键") @RequestParam Integer roleId,
            @ApiParam(value = "用户主键数组") @RequestParam Integer[] userIds){
        roleService.assignRoleToUsers(roleId, userIds);
        return ResultUtil.success();
    }
}
