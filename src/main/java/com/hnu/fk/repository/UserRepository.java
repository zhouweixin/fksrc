package com.hnu.fk.repository;

import com.hnu.fk.domain.Department;
import com.hnu.fk.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:23 2018/8/6
 * @Modified By:
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 通过主键批量删除
     *
     * @param ids
     */
    public void deleteByIdIn(Collection<Integer> ids);

    /**
     * 通过名称模糊查询-分页
     *
     * @param name
     * @param pageable
     * @return
     */
    public Page<User> findByNameLike(String name, Pageable pageable);

    /**
     * 通过部门查询-分页
     *
     * @param departments
     * @return
     */
    public Page<User> findByDepartmentIn(Collection<Department> departments, Pageable pageable);

    /**
     * 通过用户主键更新部门
     *
     * @param department
     * @param id
     */
    @Modifying
    @Query(value = "update User u set u.department=?1 where u.id=?2")
    public void updateDepartmentById(Department department, Integer id);

    /**
     * 通过用户主键更新启用
     *
     * @param enable
     * @param id
     */
    @Modifying
    @Query(value = "update User u set u.enable=?1 where u.id=?2")
    public void updateEnableById(Integer enable, Integer id);
}
