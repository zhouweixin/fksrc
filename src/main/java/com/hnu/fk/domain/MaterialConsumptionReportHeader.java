package com.hnu.fk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗统计表头
 * @Date: Created in 10:27 2018/8/21
 * @Modified By:
 */
@Entity
@Table(name = "produce_material_consumption_report_header")
@ApiModel(value = "物料消耗统计表头")
public class MaterialConsumptionReportHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键: 自增长")
    private Long id;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyyMM")
    @JsonFormat(pattern = "yyyyMM")
    @ApiModelProperty(value = "日期, 格式为yyyyMM")
    private Date date;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期, 格式为yyyy-MM-dd")
    private Date startDate;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期, 格式为yyyy-MM-dd")
    private Date endDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生成时间, 格式为yyyy-MM-dd HH:mm:ss")
    private Date enterTime;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "enter_user_id", referencedColumnName = "id")
    @ApiModelProperty(value = "生成人")
    private User enterUser;

    @ApiModelProperty(value = "本月原矿处理量")
    @Column(precision = 2)
    private Double curRawOre = 0.0;

    @ApiModelProperty(value = "累计原矿处理量")
    @Column(precision = 2)
    private Double totalRawOre = 0.0;

    @OneToMany(targetEntity = MaterialConsumptionReportDetail.class, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    @ApiModelProperty(value = "详细数据")
    private Set<MaterialConsumptionReportDetail> details;

    @OneToMany(targetEntity = MaterialConsumptionReportTypeDetail.class, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    @ApiModelProperty(value = "类型详细数据")
    private Set<MaterialConsumptionReportTypeDetail> typeDetails;

    /**
     * 无参构造函数
     */
    public MaterialConsumptionReportHeader() {
        this.enterTime = new Date();
    }

    /**
     * 有参构造函数
     *
     * @param enterUser
     * @param date
     */
    public MaterialConsumptionReportHeader(User enterUser, Date date, Date startDate, Date endDate) {
        this.date = date;
        this.enterUser = enterUser;
        this.enterTime = new Date();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public User getEnterUser() {
        return enterUser;
    }

    public void setEnterUser(User enterUser) {
        this.enterUser = enterUser;
    }

    public Double getCurRawOre() {
        return curRawOre;
    }

    public void setCurRawOre(Double curRawOre) {
        this.curRawOre = curRawOre;
    }

    public Double getTotalRawOre() {
        return totalRawOre;
    }

    public void setTotalRawOre(Double totalRawOre) {
        this.totalRawOre = totalRawOre;
    }

    public Set<MaterialConsumptionReportDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<MaterialConsumptionReportDetail> details) {
        this.details = details;
    }

    public Set<MaterialConsumptionReportTypeDetail> getTypeDetails() {
        return typeDetails;
    }

    public void setTypeDetails(Set<MaterialConsumptionReportTypeDetail> typeDetails) {
        this.typeDetails = typeDetails;
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
}
