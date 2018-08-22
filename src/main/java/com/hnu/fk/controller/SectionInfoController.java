package com.hnu.fk.controller;

import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.SectionInfo;
import com.hnu.fk.service.SectionInfoService;
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

@RestController
@RequestMapping(value ="/SectionInfo")
@Api(tags = "工段信息接口")
public class SectionInfoController {
    @Autowired
    private SectionInfoService sectionInfoService;

    /**
     * 新增
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增",notes = "主键自增长不需要传参")
    public Result<SectionInfo> add(@Valid SectionInfo sectionInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        return ResultUtil.success(sectionInfoService.add(sectionInfo));
    }

    /**
     * 更新
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<SectionInfo> update(@Valid SectionInfo sectionInfo,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        return ResultUtil.success(sectionInfoService.update(sectionInfo));
    }

    /**
     * 根据id删除
     */
    @DeleteMapping(value = "/deleteById")
    @ApiOperation(value = "根据id删除")
    public Result<Object> deleteById(@ApiParam(value = "主键") @RequestParam Integer id){
        sectionInfoService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping(value = "/deleteInBatch")
    @ApiOperation(value = "批量删除")
    public Result<Object> deleteInBatch(@ApiParam(value = "主键数组") @RequestParam Integer[] ids){
        sectionInfoService.deleteInBatch(ids);
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
    public Result<SectionInfo> getById(@ApiParam(value = "主键") @RequestParam Integer id) {
        return ResultUtil.success(sectionInfoService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<SectionInfo>> getAll() {
        return ResultUtil.success(sectionInfoService.findAll());

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
    public Result<Page<SectionInfo>> getAllByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(sectionInfoService.findAllByPage(page, size, sortFieldName, asc));
    }

    /**
     * 通过名称模糊查询-分页
     *
     * @return
     */
    @GetMapping(value = "/getByMaterialTypeAndNameLikeByPage")
    @ApiOperation(value = "通过名称模糊查询-分页")
    public Result<Page<SectionInfo>> getByNameLikeByPage(
            @ApiParam(value = "名称(默认为\"\")", defaultValue = "") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页码(从0开始)", defaultValue = "0") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)", defaultValue = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)", defaultValue = "id") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)", defaultValue = "1") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(sectionInfoService.findNameLikeByPage(page, size, sortFieldName, asc,name));
    }
}
