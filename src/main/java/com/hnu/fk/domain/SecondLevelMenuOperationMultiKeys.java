package com.hnu.fk.domain;

import java.io.Serializable;

/**
 * @Author: zhouweixin
 * @Description: 二级菜单和操作分配的联合主键
 * @Date: Created in 13:31 2018/8/6
 * @Modified By:
 */
public class SecondLevelMenuOperationMultiKeys implements Serializable {
    // 二级菜单主键
    private Integer secondLevelMenuId;
    // 操作主键
    private Integer operationId;

    public SecondLevelMenuOperationMultiKeys() {
    }

    public SecondLevelMenuOperationMultiKeys(Integer secondLevelMenuId, Integer operationId) {
        this.secondLevelMenuId = secondLevelMenuId;
        this.operationId = operationId;
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
