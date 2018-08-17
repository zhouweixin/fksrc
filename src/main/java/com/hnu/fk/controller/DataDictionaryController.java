package com.hnu.fk.controller;


import com.hnu.fk.domain.DataDictionary;
import com.hnu.fk.domain.Result;
import com.hnu.fk.service.DataDictionaryService;
import com.hnu.fk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.util.RequestUtil;
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
     * 新增
     * @param dataDictionary
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增",notes = "父类型父编码默认Integer-1")
    public Result<DataDictionary> add(@Valid DataDictionary dataDictionary, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage());
        }

        return ResultUtil.success(dataDictionaryService.save(dataDictionary));
    }

    /**
     * 更新
     * @param dataDictionary
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新",notes = "编码,名称,值均有同名校验")
    public Result<DataDictionary> update(@Valid DataDictionary dataDictionary,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(bindingResult.getFieldError().getDefaultMessage().toString());
        }

        return ResultUtil.success(dataDictionaryService.update(dataDictionary));
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteById")
    @ApiOperation(value="通过id删除",notes = "删除类型会删除其所有数据")
    public Result<Object> deleteById(@ApiParam(value = "主键") @RequestParam Long id) {
        dataDictionaryService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 根据ids批量删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteByIds")
    @ApiOperation(value = "批量删除",notes="删除类型会删除类型下所有数据")
    public Result<Object> deleteById(@ApiParam(value = "主键数组")@RequestParam Long[] ids){
        dataDictionaryService.deleteByIdIn(ids);
        return ResultUtil.success();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping(value = "/getById")
    @ApiOperation(value = "根据id查询",notes = "根据主键id查询")
    public Result<DataDictionary> getById(@ApiParam(value = "主键") @RequestParam Long id){
        return ResultUtil.success(dataDictionaryService.findOne(id));
    }

    /**
     * 根据编码查询
     * @param dicId
     * @param ParentId
     * @return
     */
    @GetMapping(value = "/getByDicId")
    @ApiOperation(value = "根据编码查询",notes ="查询时需要传入父编码")
    public Result<DataDictionary> getByDicId(@ApiParam(value = "编码") @RequestParam Integer dicId,
                                             @ApiParam(value = "父编码") @RequestParam Integer ParentId){
        return ResultUtil.success(dataDictionaryService.findOneByDicId(dicId,ParentId));
    }
    /**
     * 查询所有
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "查询所有")
    public Result<List<DataDictionary>> getAll(){
        return ResultUtil.success(dataDictionaryService.findAll());
    }

    /**
     * 根据类型编码查询其所有数据
     * @param id
     * @return
     */
    @GetMapping(value = "/getAllByParentId")
    @ApiOperation(value = "根据编码查询类型下所有数据")
    public Result<List<DataDictionary>> getAllByParentId(@ApiParam(value = "主键") @RequestParam Integer id){
        return ResultUtil.success(dataDictionaryService.findAllChildrenById(id));
    }

    /**
     * 查询所有类型
     * @return
     */
    @GetMapping(value = "/getAllParents")
    @ApiOperation(value = "查询所有类型")
    public Result<List<DataDictionary>> getAllParents(){
        return ResultUtil.success(dataDictionaryService.findAllParents());
    }

    /**
     * 分页查询所有类型
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "查询所有类型-分页")
    public Result<Page<DataDictionary>> getAllParentsByPage(
            @ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
            @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc) {

        return ResultUtil.success(dataDictionaryService.findAllParentsByPage(page, size, sortFieldName, asc));
    }

    /**
     * 分页查询一个类型下所有数据
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @param dicId
     * @return
     */
    @GetMapping(value = "/getAllChildrensByPage")
    @ApiOperation(value = "查询类型下所有数据-分页")
    public Result<Page<DataDictionary>> getAllChilrensByPage(@ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
      @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
      @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
      @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc,
      @ApiParam(value = "类型编码") @RequestParam Integer dicId){
        return  ResultUtil.success(dataDictionaryService.findAllChildrensByPageById(page,size,sortFieldName,asc,dicId));
    }

    /**
     * 分页模糊查询一个类型下所有数据
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @param dicId
     * @param name
     * @return
     */
    @GetMapping(value = "/getChildrensByPageNameLike")
    @ApiOperation(value = "模糊查询类型下所有数据-分页")
    public Result<Page<DataDictionary>> getChilrensByPageNameLike(@ApiParam(value = "名称(默认为\"\")") @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @ApiParam(value = "每页记录数(默认为10)") @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                             @ApiParam(value = "排序字段名(默认为id)") @RequestParam(value = "sortFieldName", defaultValue = "id") String sortFieldName,
                                                             @ApiParam(value = "排序方向(0:降序；1升序；默认为1)") @RequestParam(value = "asc", defaultValue = "1") Integer asc,
                                                             @ApiParam(value = "类型编码") @RequestParam Integer dicId,@ApiParam(value = "数据名") @RequestParam String name){
        return  ResultUtil.success(dataDictionaryService.findChildrensByPageNameLikeById(page,size,sortFieldName,asc,dicId,name));
    }

}
