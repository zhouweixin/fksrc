package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 用户
 * @Date: Created in 14:17 2018/8/6
 * @Modified By:
 */
@Entity
@Table(name = "permission_user")
@ApiModel(description = "用户")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键:自增长")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("部门")
    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("联系方式")
    private String contact;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("是否启用:0不启用;1启用;(默认值为1)")
    private Integer enable = 1;
}
