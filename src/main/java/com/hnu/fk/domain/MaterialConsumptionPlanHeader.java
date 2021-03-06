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
 * @Description: 物料消耗计划表头
 * @Date: Created in 10:06 2018/8/21
 * @Modified By:
 */
@Entity
@Table(name = "produce_material_consumption_plan_header")
@ApiModel(value = "物料消耗计划表头")
public class MaterialConsumptionPlanHeader {
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
    @ApiModelProperty(value = "录入时间, 格式为yyyy-MM-dd HH:mm:ss")
    private Date enterTime;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "enter_user_id", referencedColumnName = "id")
    @ApiModelProperty(value = "录入人")
    private User enterUser;

    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间, 格式为yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "modify_user_id", referencedColumnName = "id")
    @ApiModelProperty(value = "修改人")
    private User modifyUser;

    @ApiModelProperty(value = "标志: 0未提交; 1已提交")
    private Integer flag = 0;

    @OneToMany(targetEntity = MaterialConsumptionPlanDetail.class, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    @ApiModelProperty(value = "详细数据")
    private List<MaterialConsumptionPlanDetail> details;

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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public User getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(User modifyUser) {
        this.modifyUser = modifyUser;
    }

    public List<MaterialConsumptionPlanDetail> getDetails() {
        return details;
    }

    public void setDetails(List<MaterialConsumptionPlanDetail> details) {
        this.details = details;
    }
}
