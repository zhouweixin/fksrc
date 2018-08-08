package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @Author: zhouweixin
 * @Description: 用户角色关系
 * @Date: Created in 14:25 2018/8/6
 * @Modified By:
 */
@Entity
@Table(name = "permission_user_role")
@ApiModel(description = "用户角色关系")
@IdClass(value = UserRoleMultiKeys.class)
public class UserRole {
    @Id
    @ApiModelProperty("用户主键")
    private Integer userId;
    @Id
    @ApiModelProperty("角色主键")
    private Integer roleId;

    public UserRole() {
    }

    public UserRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
