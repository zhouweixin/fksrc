package com.hnu.fk.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zhouweixin
 * @Description: 操作日志
 * @Date: Created in 14:34 2018/8/6
 * @Modified By:
 */
@Entity
@Table(name = "permission_action_log")
@ApiModel(description = "操作日志")
public class ActionLog {
    @Id
    @ApiModelProperty("主键: 自增长")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "序号", orderNum = "0")
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ApiModelProperty("用户")
    @ExcelEntity(id = "Operator")
    private User user;

    @ApiModelProperty("操作对象")
    @Excel(name = "操作对象",orderNum = "2")
    private String object;

    @ApiModelProperty("操作类型")
    @Excel(name = "操作类型",orderNum = "3")
    private String type;

    @ApiModelProperty("操作数据描述")
    @Excel(name = "操作数据描述",orderNum = "4")
    private String description;

    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("时间")
    @Excel(name = "时间",orderNum = "5")
    private Date time = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
