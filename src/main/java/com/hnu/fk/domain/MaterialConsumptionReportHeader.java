package com.hnu.fk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    private Double byykcll;

    @ApiModelProperty(value = "累计原矿处理量")
    @Column(precision = 2)
    private Double ljykcll;

    @ApiModelProperty(value = "本月药剂成本")
    @Column(precision = 2)
    private Double byyjcb;

    @ApiModelProperty(value = "累计药剂成本")
    @Column(precision = 2)
    private Double ljyjcb;

    @ApiModelProperty(value = "本月药剂单位成本")
    @Column(precision = 2)
    private Double byyjdwcb;

    @ApiModelProperty(value = "累计药剂单位成本")
    @Column(precision = 2)
    private Double ljyjdwcb;

    @ApiModelProperty(value = "本月钢球成本")
    @Column(precision = 2)
    private Double bygqcb;

    @ApiModelProperty(value = "累计钢球成本")
    @Column(precision = 2)
    private Double ljgqcb;

    @ApiModelProperty(value = "本月钢球单位成本")
    @Column(precision = 2)
    private Double bygqdwcb;

    @ApiModelProperty(value = "累计钢球单位成本")
    @Column(precision = 2)
    private Double ljgqdwcb;

    @OneToMany(targetEntity = MaterialConsumptionReportDetail.class, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    @ApiModelProperty(value = "详细数据")
    private List<MaterialConsumptionReportDetail> details;

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

    public Double getByykcll() {
        return byykcll;
    }

    public void setByykcll(Double byykcll) {
        this.byykcll = byykcll;
    }

    public Double getLjykcll() {
        return ljykcll;
    }

    public void setLjykcll(Double ljykcll) {
        this.ljykcll = ljykcll;
    }

    public Double getByyjcb() {
        return byyjcb;
    }

    public void setByyjcb(Double byyjcb) {
        this.byyjcb = byyjcb;
    }

    public Double getLjyjcb() {
        return ljyjcb;
    }

    public void setLjyjcb(Double ljyjcb) {
        this.ljyjcb = ljyjcb;
    }

    public Double getByyjdwcb() {
        return byyjdwcb;
    }

    public void setByyjdwcb(Double byyjdwcb) {
        this.byyjdwcb = byyjdwcb;
    }

    public Double getLjyjdwcb() {
        return ljyjdwcb;
    }

    public void setLjyjdwcb(Double ljyjdwcb) {
        this.ljyjdwcb = ljyjdwcb;
    }

    public Double getBygqcb() {
        return bygqcb;
    }

    public void setBygqcb(Double bygqcb) {
        this.bygqcb = bygqcb;
    }

    public Double getLjgqcb() {
        return ljgqcb;
    }

    public void setLjgqcb(Double ljgqcb) {
        this.ljgqcb = ljgqcb;
    }

    public Double getBygqdwcb() {
        return bygqdwcb;
    }

    public void setBygqdwcb(Double bygqdwcb) {
        this.bygqdwcb = bygqdwcb;
    }

    public Double getLjgqdwcb() {
        return ljgqdwcb;
    }

    public void setLjgqdwcb(Double ljgqdwcb) {
        this.ljgqdwcb = ljgqdwcb;
    }

    public List<MaterialConsumptionReportDetail> getDetails() {
        return details;
    }

    public void setDetails(List<MaterialConsumptionReportDetail> details) {
        this.details = details;
    }
}
