package com.hnu.fk.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 说明:统计时间区间表
 *
 * @author WaveLee
 * 日期: 2018/8/13
 */
@Entity
@Table(name = "basicinfo_temporal_interval")
@ApiModel(description = "月统计时间区间")
@ExcelTarget("temporalIntervalEntity")
public class TemporalInterval {
    /**
     * 序号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键：自增长")
    @Excel(name = "序号", orderNum = "0")
    private Long id;
    /**
     * 统计年份
     */
    @ApiModelProperty("统计年份")
    @Excel(name = "统计年份", orderNum = "1")
    private String statisticalYear;
    /**
     * 统计月份
     */
    @ApiModelProperty("统计月份")
    @Excel(name = "统计月份", orderNum = "2")
    private String statisticalMonth;
    /**
     * 起始日期
     */
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("起始日期")
    @Excel(name = "起始日期",exportFormat="yyyy-MM-dd", orderNum = "3")
    private Date startDate;
    /**
     * 结束日期
     */
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("结束日期")
    @Excel(name = "结束日期",exportFormat="yyyy-MM-dd", orderNum = "4")
    private Date endDate;
    /**
     * 起始班次
     */
    @ApiModelProperty("起始班次")
    @Excel(name = "起始班次", orderNum = "5")
    private String startShift;
    /**
     * 结束班次
     */
    @ApiModelProperty("结束班次")
    @Excel(name = "结束班次", orderNum = "6")
    private String endShift;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Excel(name = "备注", orderNum = "7")
    private String description;
    /**
     * 数据更新时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("数据更新时间")
    @Excel(name = "数据更新时间",exportFormat="yyyy-MM-dd HH:mm:ss", orderNum = "8")
    private Date updateTime;
    /**
     * 数据更新人
     */
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "update_user", referencedColumnName = "id")
    @ApiModelProperty("数据更新人")
    @ExcelEntity(id = "tiUpdater")
    private User updateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatisticalYear() {
        return statisticalYear;
    }

    public void setStatisticalYear(String statisticalYear) {
        this.statisticalYear = statisticalYear;
    }

    public String getStatisticalMonth() {
        return statisticalMonth;
    }

    public void setStatisticalMonth(String statisticalMonth) {
        this.statisticalMonth = statisticalMonth;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStartShift() {
        return startShift;
    }

    public void setStartShift(String startShift) {
        this.startShift = startShift;
    }

    public String getEndShift() {
        return endShift;
    }

    public void setEndShift(String endShift) {
        this.endShift = endShift;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        return "TemporalInterval{" +
                "id=" + id +
                ", statisticalYear='" + statisticalYear + '\'' +
                ", statisticalMonth='" + statisticalMonth + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startShift='" + startShift + '\'' +
                ", endShift='" + endShift + '\'' +
                ", description='" + description + '\'' +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                '}';
    }
}
