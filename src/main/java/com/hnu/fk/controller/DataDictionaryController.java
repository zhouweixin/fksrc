package com.hnu.fk.controller;


import com.hnu.fk.domain.DataDictionary;
import com.hnu.fk.domain.DataDictionaryType;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.DataDictionaryService;
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
 * @Author: huXuDong
 * @Description: 数据字典
 * @Date: Created in 10:20 2018/8/15
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/dataDictionary")
@Api(tags = "数据字典")
public class DataDictionaryController {
    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 新增类型
     * @return
     */
    @PostMapping(value = "/addType")
    @ApiOperation(value = "新增类型")
    public Result<DataDictionaryType> addType(@Valid DataDictionaryType dataDictionaryType, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage());
        }

        return ResultUtil.success(dataDictionaryService.saveType(dataDictionaryType));
    }

    /**
     * 新增数据
     * @param dataDictionary
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/addData")
    @ApiOperation(value ="新增数据")
    public Result<DataDictionary> addData(@Valid DataDictionary dataDictionary,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage());
        }

        return ResultUtil.success(dataDictionaryService.saveData(dataDictionary));
    }

    /**
     * 更新类型
     */
    @PostMapping(value = "/updateType")
    @ApiOperation(value = "更新类型")
    public Result<DataDictionaryType> updateType(@Valid DataDictionaryType dataDictionaryType,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(dataDictionaryService.updateType(dataDictionaryType));
    }

    /**
     * 更新数据
     * @param dataDictionary
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/updateData")
    @ApiOperation(value = "更新数据")
    public Result<DataDictionary> updateDate(@Valid DataDictionary dataDictionary,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(dataDictionaryService.updateData(dataDictionary));
    }

    /**
     * 根据id删除类型
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteTypeById")
    @ApiOperation(value="通过id删除类型",notes = "删除类型会删除其所有数据")
    public Result<Object> deleteTypeById(@ApiParam(value = "主键") @RequestParam Long id) {
        dataDictionaryService.deleteType(id);
        return ResultUtil.success();
    }

    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteDataById")
    @ApiOperation(value = "通过id删除数据")
    public Result<Object> deleteDataById(@ApiParam(value = "主键")@RequestParam Long id){
        dataDictionaryService.deleteData(id);
        return ResultUtil.success();
    }

    /**
     * 根据ids批量删除类型
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteTypesByIds")
    @ApiOperation(value = "批量删除类型",notes="删除类型会删除类型下所有数据")
    public Result<Object> deleteTypeByIds(@ApiParam(value = "主键数组")@RequestParam Long[] ids){
        dataDictionaryService.deleteTypeInbatch(ids);
        return ResultUtil.success();
    }

    /**
     * 根据ids批量删除数据
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteDataByIds")
    @ApiOperation(value = "批量删除数据")
    public Result<Object> deleteDataById(@ApiParam(value = "主键数组")@RequestParam Long[] ids){
        dataDictionaryService.deleteDataInBatch(ids);
        return ResultUtil.success();
    }

    /**
     * 根据id查询类型
     * @param id
     * @return
     */
    @GetMapping(value = "/getTypeById")
    @ApiOperation(value = "根据id查询类型")
    public Result<DataDictionaryType> getTypeById(@ApiParam(value = "主键") @RequestParam Long id){
        return ResultUtil.success(dataDictionaryService.findOneType(id));
    }

    /**
     * 根据id查询数据
     */
    @GetMapping(value = "/getDataById")
    @ApiOperation(value = "根据id查询数据")
    public Result<DataDictionary> getDataById(@ApiParam(value = "主键") @RequestParam Long id){
        return ResultUtil.success(dataDictionaryService.findOneData(id));
    }


    /**
     * 根据类型主键查询其所有数据
     * @param id
     * @return
     */
    @GetMapping(value = "/getAllDataByTypeId")
    @ApiOperation(value = "查询类型下所有数据")
    public Result<List<DataDictionary>> getAllDataByTypeId(@ApiParam(value = "主键") @RequestParam Long id){
        return ResultUtil.success(dataDictionaryService.findAllDataByTypeId(id));
    }

    /**
     * 查询所有类型
     * @return
     */
    @GetMapping(value = "/getAllTypes")
    @ApiOperation(value = "查询所有类型")
    public Result<List<DataDictionaryType>> getAllTypes(){
        return ResultUtil.success(dataDictionaryService.findAllTypes());
    }

    /**
     * 分页查询所有类型
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    @GetMapping(value = "/getAllTypesByPage")
    @ApiOperation(value = "查询所有类型-分页")
    public Result<Page<DataDictionaryType>> getAllTypesByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(dataDictionaryService.findAllTypesByPage(page, size, sortFieldName, asc));
    }

    /**
     * 模糊查询所有类型—分页
     */
    @GetMapping(value = "/getAllTypesByNameLikeByPage")
    @ApiOperation(value = "模糊查询所有类型-分页")
    public Result<Page<DataDictionaryType>> getAllTypesByNameLikeByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc,
            @ApiParam(value="类型名称") @RequestParam String typeName) {

        return ResultUtil.success(dataDictionaryService.findAllTypeNameLikeByPage(page, size, sortFieldName, asc,typeName));
    }
    /**
     * 分页查询一个类型下所有数据
     */
    @GetMapping(value = "/getAllDataByTypeByPage")
    @ApiOperation(value = "查询类型下所有数据-分页")
    public Result<Page<DataDictionary>> getAllDataByPage(@ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
      @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
      @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
      @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc,
      @ApiParam(value = "类型主键") @RequestParam Long id){
        return  ResultUtil.success(dataDictionaryService.findAllDataByPageByTypeId(page,size,sortFieldName,asc,id));
    }

    /**
     * 分页模糊查询一个类型下所有数据
     */
    @GetMapping(value = "/getAllDataByPageNameLike")
    @ApiOperation(value = "模糊查询类型下所有数据-分页")
    public Result<Page<DataDictionary>> getAllDataByPageNameLikeByTypeId(@ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                             @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
                                                             @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc,
                                                             @ApiParam(value = "类型主键") @RequestParam Long id,@ApiParam(value = "数据名") @RequestParam String name){
        return  ResultUtil.success(dataDictionaryService.findDataByPageNameLikeById(page,size,sortFieldName,asc,id,name));
    }

}
