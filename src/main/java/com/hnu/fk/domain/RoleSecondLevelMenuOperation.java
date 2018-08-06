package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 13:51 2018/8/6
 * @Modified By:
 */
@Entity
@Table(name = "permission_role_second_level_menu_operation")
@ApiModel(description = "角色,二级菜单和操作关系")
@IdClass(value = RoleSecondLevelMenuOperationMultiKeys.class)
public class RoleSecondLevelMenuOperation {
    // 角色主键
    @Id
    private Integer roleId;
    // 二级菜单主键
    @Id
    private Integer secondLevelMenuId;
    // 操作主键
    @Id
    private Integer operationId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getSecondLevelMenuId() {
        return secondLevelMenuId;
    }

    public void setSecondLevelMenuId(Integer secondLevelMenuId) {
        this.secondLevelMenuId = secondLevelMenuId;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }
}
