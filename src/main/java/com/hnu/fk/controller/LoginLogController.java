package com.hnu.fk.controller;

import com.hnu.fk.domain.LoginLog;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.LoginLogService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 说明:登录日志接口
 *
 * @author WaveLee
 * 日期: 2018/8/8
 */
@RestController
@RequestMapping(value = "loginLog")
@Api(tags = "登录日志接口")
public class LoginLogController {
    @Autowired
    private LoginLogService loginLogService;

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
    public Result<Page<LoginLog>> getAllByPage(
            @ApiParam(value = "页码(默认为0)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；这里默认为0)") @RequestParam(value = "asc", defaultValue = "0") Integer asc) {

        return ResultUtil.success(loginLogService.findAllByPage(page, size, sortFieldName, asc));
    }

    @GetMapping(value = "/getByDate")
    @ApiOperation(value = "通过日期模糊查询")
    public Result<Page<LoginLog>> getByDateLikeByPage(
            @ApiParam(value = "日期，格式为yyyy-MM-dd") @RequestParam(value = "date") Date date,
            @ApiParam(value = "页码(默认为0)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；这里默认为0)") @RequestParam(value = "asc", defaultValue = "0") Integer asc) {

        return ResultUtil.success(loginLogService.findByDateLike(date, page, size, sortFieldName, asc));
    }
}
