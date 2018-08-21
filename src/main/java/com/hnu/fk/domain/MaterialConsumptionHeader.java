package com.hnu.fk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗信息表头
 * @Date: Created in 9:30 2018/8/21
 * @Modified By:
 */
@Entity
@Table(name = "produce_material_consumption_header")
@ApiModel(value = "物料消耗表头")
public class MaterialConsumptionHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键: 自增长")
    private Long id;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期, 格式为yyyy-MM-dd")
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

    @OneToMany(targetEntity = MaterialConsumptionDetail.class, cascade = {CascadeType.ALL})
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    private List<MaterialConsumptionDetail> materialConsumptionDetails;

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

    public List<MaterialConsumptionDetail> getMaterialConsumptionDetails() {
        return materialConsumptionDetails;
    }

    public void setMaterialConsumptionDetails(List<MaterialConsumptionDetail> materialConsumptionDetails) {
        this.materialConsumptionDetails = materialConsumptionDetails;
    }
}
