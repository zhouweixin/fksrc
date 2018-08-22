package com.hnu.fk.controller;

import com.hnu.fk.domain.Result;
import com.hnu.fk.domain.StandingBookType;
import com.hnu.fk.service.StandingBookTypeService;
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
@RequestMapping(value = "/standingBookType")
@Api(tags = "调度台账项目类型")
public class StandingBookTypeController {
    @Autowired
    private StandingBookTypeService standingBookTypeService;
    /**
     * 新增
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增",notes = "主键自增长不需要传参")
    public Result<StandingBookType> add(@Valid StandingBookType standingBookType, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        return ResultUtil.success(standingBookTypeService.add(standingBookType));
    }

    /**
     * 更新
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新")
    public Result<StandingBookType> update(@Valid StandingBookType standingBookType,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        return ResultUtil.success(standingBookTypeService.update(standingBookType));
    }

    /**
     * 根据id删除
     */
    @DeleteMapping(value = "/deleteById")
    @ApiOperation(value = "根据id删除")
    public Result<Object> deleteById(@ApiParam(value = "主键") @RequestParam Integer id){
        standingBookTypeService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping(value = "/deleteInBatch")
    @ApiOperation(value = "批量删除")
    public Result<Object> deleteInBatch(@ApiParam(value = "主键数组") @RequestParam Integer[] ids){
        standingBookTypeService.deleteInBatch(ids);
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
    public Result<StandingBookType> getById(@ApiParam(value = "主键") @RequestParam Integer id) {
        return ResultUtil.success(standingBookTypeService.findOne(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<StandingBookType>> getAll() {
        return ResultUtil.success(standingBookTypeService.findAll());

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
    public Result<Page<StandingBookType>> getAllByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(standingBookTypeService.findAllByPage(page, size, sortFieldName, asc));
    }

    /**
     * 根据所属工段和台账项目类型名称模糊查询-分页
     *
     * @return
     */
    @GetMapping(value = "/getByMaterialTypeAndNameLikeByPage")
    @ApiOperation(value = "通过所属工段和名称模糊查询-分页")
    public Result<Page<StandingBookType>> getByNameLikeByPage(
            @ApiParam(value = "工段信息主键,默认为-1,表示不启用",defaultValue = "-1") @RequestParam(value = "SectionInfoId",defaultValue = "-1") Integer sectionId,
            @ApiParam(value = "名称(默认为\"\")", defaultValue = "") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页码(从0开始)", defaultValue = "0") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)", defaultValue = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)", defaultValue = "id") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)", defaultValue = "1") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(standingBookTypeService.findByNameLikeAndSectionByPage(page, size, sortFieldName, asc,name,sectionId));
    }
}
