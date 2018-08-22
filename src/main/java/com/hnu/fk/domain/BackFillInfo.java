package com.hnu.fk.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "produce_back_fill_info")
@ApiModel(description = "检修计划表")
public class BackFillInfo {
    @Id
    @ApiModelProperty("序号，主键: 自增长")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "回填工单序号")
    private Integer id;

    @ManyToOne(targetEntity = MaintenanceSchedule.class)
    @JoinColumn(name = "maintenance_schedule_code", referencedColumnName = "id")
    @ApiModelProperty("检修计划")
    @ExcelEntity
    private MaintenanceSchedule maintenanceSchedule;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_code_enter", referencedColumnName = "id")
    @ApiModelProperty("维修人")
    @ExcelEntity(id = "bfiEnter")
    private User backEnter;

    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("到达时间")
    @Excel(name = "到达时间",width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "8")
    private Date arriveTime;

    @ApiModelProperty("故障原因")
    @Excel(name = "故障原因",width = 20,orderNum = "9")
    private String cause;

    @ApiModelProperty("处理结果")
    @Excel(name = "处理结果",width = 20,orderNum = "10")
    private String result;

    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("完成时间")
    @Excel(name = "完成时间",width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "11")
    private Date finishTime;

    @ApiModelProperty("备注")
    @Excel(name = "回填工单备注",width = 20,orderNum = "12")
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MaintenanceSchedule getMaintenanceSchedule() {
        return maintenanceSchedule;
    }

    public void setMaintenanceSchedule(MaintenanceSchedule maintenanceSchedule) {
        this.maintenanceSchedule = maintenanceSchedule;
    }

    public User getBackEnter() {
        return backEnter;
    }

    public void setBackEnter(User backEnter) {
        this.backEnter = backEnter;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "BackFillInfo{" +
                "id=" + id +
                ", maintenanceSchedule=" + maintenanceSchedule +
                ", backEnter=" + backEnter +
                ", arriveTime=" + arriveTime +
                ", cause='" + cause + '\'' +
                ", result='" + result + '\'' +
                ", finishTime=" + finishTime +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
