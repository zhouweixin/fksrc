package com.hnu.fk.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
@Entity
@Table(name = "produce_maintenance_schedule")
@ApiModel(description = "检修计划表")
@ExcelTarget(value = "maintenanceScheduleEntity")
public class MaintenanceSchedule {
    @Id
    @ApiModelProperty("序号，主键: 自增长")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "检修计划序号")
    private Integer id;

    @ApiModelProperty("设备ID")
    @ManyToOne(targetEntity = EquipmentInfo.class)
    @JoinColumn(name = "equipment_code", referencedColumnName = "id")
    @ExcelEntity(id = "msEquipment")
    private EquipmentInfo equipmentInfoId;

    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("报修时间，自动录入当前时间")
    @Excel(name = "报修时间",width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "2")
    private Date enteringTime;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_code_enter", referencedColumnName = "id")
    @ApiModelProperty("报修人，自动录入当前用户")
    @ExcelEntity(id = "msEnter")
    private User enter;

    @ApiModelProperty("故障描述")
    @Excel(name = "故障描述",width = 20,orderNum = "3")
    private String description;

    @ApiModelProperty("备注")
    @Excel(name = "检修计划备注",width = 20,orderNum = "4")
    private String remarks;

    @ApiModelProperty("标志位，0未完成，1已完成")
    @Excel(name = "标志位",replace = { "未完成_0", "已完成_1" },orderNum = "5")
    private Integer flag;

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getFlag() {
        return flag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EquipmentInfo getEquipmentInfoId() {
        return equipmentInfoId;
    }

    public void setEquipmentInfoId(EquipmentInfo equipmentInfoId) {
        this.equipmentInfoId = equipmentInfoId;
    }

    public Date getEnteringTime() {
        return enteringTime;
    }

    public void setEnteringTime(Date enteringTime) {
        this.enteringTime = enteringTime;
    }

    public User getEnter() {
        return enter;
    }

    public void setEnter(User enter) {
        this.enter = enter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "MaintenanceSchedule{" +
                "id=" + id +
                ", equipmentInfoId=" + equipmentInfoId +
                ", enteringTime=" + enteringTime +
                ", enter=" + enter +
                ", description='" + description + '\'' +
                ", remarks='" + remarks + '\'' +
                ", flag=" + flag +
                '}';
    }
}
