package com.hnu.fk.domain;

import java.io.Serializable;

/**
 * @Author: zhouweixin
 * @Description: 用户角色对应关系的联合主键
 * @Date: Created in 14:25 2018/8/6
 * @Modified By:
 */
public class UserRoleMultiKeys implements Serializable {
    // 用户主键
    private Integer userId;
    // 角色主键
    private Integer roleId;

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
