package com.hnu.fk.domain;

import java.io.Serializable;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 13:51 2018/8/6
 * @Modified By:
 */
public class RoleSecondLevelMenuOperationMultiKeys implements Serializable {
    // 角色主键
    private Integer roleId;
    // 二级菜单主键
    private Integer secondLevelMenuId;
    // 操作主键
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
