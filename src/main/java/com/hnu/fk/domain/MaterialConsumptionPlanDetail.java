package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗计划明细
 * @Date: Created in 10:18 2018/8/21
 * @Modified By:
 */
@Entity
@Table(name = "produce_material_consumption_plan_detail")
@ApiModel(value = "物料消耗计划明细")
public class MaterialConsumptionPlanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键: 自增长")
    private Long id;

    @ManyToOne(targetEntity = MaterialConsumptionPlanHeader.class)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    @ApiModelProperty(value = "物料消耗计划表头")
    private MaterialConsumptionPlanHeader header;

    @ManyToOne(targetEntity = MaterialConsumptionItem.class)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ApiModelProperty(value = "物料消耗项目")
    private MaterialConsumptionItem item;

    @ApiModelProperty(value = "单价")
    @Column(precision = 2)
    private Double price;

    @ApiModelProperty(value = "计划单耗")
    @Column(precision = 2)
    private Double planUnitConsumption;

    @ApiModelProperty(value = "库存量")
    @Column(precision = 2)
    private Double storage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MaterialConsumptionPlanHeader getHeader() {
        return header;
    }

    public void setHeader(MaterialConsumptionPlanHeader header) {
        this.header = header;
    }

    public MaterialConsumptionItem getItem() {
        return item;
    }

    public void setItem(MaterialConsumptionItem item) {
        this.item = item;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPlanUnitConsumption() {
        return planUnitConsumption;
    }

    public void setPlanUnitConsumption(Double planUnitConsumption) {
        this.planUnitConsumption = planUnitConsumption;
    }

    public Double getStorage() {
        return storage;
    }

    public void setStorage(Double storage) {
        this.storage = storage;
    }
}
