package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 部门
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@Entity
@Table(name = "basicinfo_department")
@ApiModel(description = "部门")
public class Department {
    /**
     * 主键:自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键:自增长")
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("信息")
    private String info;

    @ApiModelProperty("所属部门")
    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "parent_department_id", referencedColumnName = "id")
    private Department parentDepartment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", parentDepartment=" + parentDepartment +
                '}';
    }
}
