package com.hnu.fk.controller;

import com.hnu.fk.domain.Department;
import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.User;
import com.hnu.fk.service.RoleService;
import com.hnu.fk.service.UserService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public Result<User> add(@Valid User user){
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
    public Result<User> login(Integer id, String password){
        return ResultUtil.success(userService.login(id, password));
    }
}
