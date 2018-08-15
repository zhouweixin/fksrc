package com.hnu.fk.service;

import com.hnu.fk.domain.Department;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.DepartmentRepository;
import com.hnu.fk.utils.ActionLogUtil;
import com.hnu.fk.utils.LoginLogUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 新增
     *
     * @param department
     * @return
     */
    public Department save(Department department,Integer parentId) {

        // 验证是否存在
        if (department == null || (department.getId() != null && departmentRepository.findById(department.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        // 验证父部门是否存在
        if((parentId != -1) && departmentRepository.existsById(parentId)){
            department.setParentDepartment(departmentRepository.getOne(parentId));
        }else if(parentId != -1 && !departmentRepository.existsById(parentId)){
            throw new FkExceptions(EnumExceptions.ADD_UPDATE_FAILED_PARENT_NOT_EXIST);
        }

        ActionLogUtil.log("部门",0,department);

        return departmentRepository.save(department);
    }

    /**
     * 更新
     *
     * @param department
     * @return
     */
    public Department update(Department department, Integer parentId) {


        // 验证是否存在
        if (department == null || department.getId() == null || departmentRepository.findById(department.getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }
        if(parentId == -1){
            department.setParentDepartment(null);
        }else if(departmentRepository.existsById(parentId)){

            // 查询父部门的所有父部门
            Department dep = departmentRepository.findById(parentId).get();
            Set<Integer> depIds = new HashSet<Integer>();
            while(dep != null){
                depIds.add(dep.getId());
                dep = dep.getParentDepartment();
            }

            // 判断该部门是否是父部门里的一员
            if(depIds.contains(department.getId())){
                throw new FkExceptions(EnumExceptions.UPDATE_FAILED_SON_NOT_PARENT);
            }

            department.setParentDepartment(departmentRepository.getOne(parentId));
        }else if(!departmentRepository.existsById(parentId)){
            throw new FkExceptions(EnumExceptions.ADD_UPDATE_FAILED_PARENT_NOT_EXIST);
        }

        Department departmentOld = departmentRepository.getOne(department.getId());
        ActionLogUtil.log("部门",departmentOld,department);

        return departmentRepository.save(department);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {

        // 验证是否存在
        if (departmentRepository.findById(id).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }

        Department department = departmentRepository.getOne(id);
        ActionLogUtil.log("部门",1,department);

        departmentRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {

        ActionLogUtil.log("部门", 1, departmentRepository.findAllById(Arrays.asList(ids)));

        departmentRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public Department findOne(Integer id) {
        Optional<Department> optional = departmentRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<Department> findAll() {
        return departmentRepository.findAll();
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
    public Page<Department> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            Department.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            // 如果不存在就设置为id
            sortFieldName = "id";
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        Pageable pageable =PageRequest.of(page, size, sort);
        return departmentRepository.findAll(pageable);
    }

    /**
     * 通过名称模糊分页查询
     *
     * @param name
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<Department> findByNameLikeByPage(String name, Integer page, Integer size, String sortFieldName,
                                             Integer asc) {

        // 判断排序字段名是否存在
        try {
            Department.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            // 如果不存在就设置为id
            sortFieldName = "id";
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        Pageable pageable =PageRequest.of(page, size, sort);
        return departmentRepository.findByNameLike("%" + name + "%", pageable);
    }

    /**
     * 通过父部门查询所有的子部门及本身
     *
     * @param department
     * @return
     */
    public List<Department> findSonAndSelfByParentDepartment(Department department){
        List<Department> departments = new ArrayList<>();

        if(department == null){
            return departments;
        }

        department.setParentDepartment(null);
        departments.add(department);

        List<Department> deps = departmentRepository.findByParentDepartment(department);
        if (deps != null && deps.size() > 0) {
            for(Department dep : deps){
                departments.addAll(findSonAndSelfByParentDepartment(dep));
            }
        }
        return departments;
    }
}
