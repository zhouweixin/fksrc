package com.hnu.fk.controller;

import com.hnu.fk.domain.DefaultPassword;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.DefaultPasswordService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 说明: 默认密码管理控制器
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
    @ApiOperation(value = "getDefaultPassword", notes = "获取默认密码")
    public Result<DefaultPassword> getDefaultPassword() {
        return ResultUtil.success(defaultPasswordService.getDefaultPassword());
    }

    /**
     * 更新默认密码
     *
     * @param defaultPassword String
     * @return DefaultPassword
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "updateDefaultPassword", notes = "更新默认密码")
    @ApiImplicitParam(value = "密码", name = "defaultPassword", defaultValue = "123456", required = true, dataType = "string", paramType = "query")
    public Result<DefaultPassword> updateDefaultPassword(@RequestParam String defaultPassword) {
        return ResultUtil.success(defaultPasswordService.updateDefaultPassword(defaultPassword));
    }
}
