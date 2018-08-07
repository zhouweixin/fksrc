package com.hnu.fk.controller;

import com.hnu.fk.domain.DefaultPassword;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.DefaultPasswordService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 说明:
 * <br>
 *
 * @author ZSCDumin
 * <br>
 * 邮箱: 2712220318@qq.com
 * <br>
 * 日期: 2018/8/7
 * <br>
 * 版本: 1.0
 */

@RestController
@RequestMapping(value = "/defaultpassword")
@Api(tags = "默认密码")
public class DefaultPasswordController {

    @Autowired
    private DefaultPasswordService defaultPasswordService;

    /**
     * 获取默认密码
     *
     * @return DefaultPassword
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "获取默认密码", notes = "获取默认密码")
    public Result<DefaultPassword> getDefaultPassword() {
        return ResultUtil.success(defaultPasswordService.getDefaultPassword());
    }

    /**
     * 更新默认密码
     *
     * @param password String
     * @return DefaultPassword
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新默认密码", notes = "更新默认密码")
    public Result<DefaultPassword> updateDefaultPassword(@RequestParam String password) {
        return ResultUtil.success(defaultPasswordService.updateDefaultPassword(password));
    }
}
