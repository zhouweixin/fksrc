package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @Author: zhouweixin
 * @Description: 二级菜单和操作分配
 * @Date: Created in 13:28 2018/8/6
 * @Modified By:
 */
@Entity
@Table(name = "permission_second_level_menu_operation")
@ApiModel(description = "二级菜单和操作关系")
@IdClass(value = SecondLevelMenuOperationMultiKeys.class)
public class SecondLevelMenuOperation {
    @Id
    @ApiModelProperty("二级菜单主键")
    private Integer secondLevelMenuId;
    @Id
    @ApiModelProperty("操作主键")
    private Integer operationId;

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
