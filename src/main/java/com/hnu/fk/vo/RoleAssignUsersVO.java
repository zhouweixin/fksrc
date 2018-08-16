package com.hnu.fk.vo;

import com.hnu.fk.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 10:33 2018/8/15
 * @Modified By:
 */
@ApiModel(value = "角色分配的用户")
public class RoleAssignUsersVO {
    @ApiModelProperty(value = "已分配的用户")
    private List<User> assignUsers;

    @ApiModelProperty(value = "未分配的用户")
    private List<User> unassignUsers;

    public RoleAssignUsersVO() {
    }

    public RoleAssignUsersVO(List<User> assignUsers, List<User> allUsers) {
        this.assignUsers = assignUsers;

        // 统计所有的已分配的用户
        Set<Integer> assignUserIds = new HashSet<>();
        for(User user : assignUsers){
            assignUserIds.add(user.getId());
        }

        // 统计所有的未分配的用户
        this.unassignUsers = new ArrayList<>();
        for(User user : allUsers){
            if(!assignUserIds.contains(user.getId())){
                this.unassignUsers.add(user);
            }
        }
    }

    public List<User> getAssignUsers() {
        return assignUsers;
    }

    public void setAssignUsers(List<User> assignUsers) {
        this.assignUsers = assignUsers;
    }

    public List<User> getUnassignUsers() {
        return unassignUsers;
    }

    public void setUnassignUsers(List<User> unassignUsers) {
        this.unassignUsers = unassignUsers;
    }
}
