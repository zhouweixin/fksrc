package com.hnu.fk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗统计明细
 * @Date: Created in 10:44 2018/8/21
 * @Modified By:
 */
@Entity
@Table(name = "produce_material_consumption_report_detail")
@ApiModel(value = "物料消耗统计明细")
public class MaterialConsumptionReportDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键: 自增长")
    private Long id;

    @ManyToOne(targetEntity = MaterialConsumptionReportHeader.class)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    @ApiModelProperty(value = "物料消耗计划表头")
    private MaterialConsumptionReportHeader header;

    @ManyToOne(targetEntity = MaterialConsumptionItem.class)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ApiModelProperty(value = "物料消耗项目")
    private MaterialConsumptionItem item;

    @ApiModelProperty(value = "单价")
    @Column(precision = 2)
    private Double price = 0.0;

    @ApiModelProperty(value = "计划单耗")
    @Column(precision = 2)
    private Double planUnitConsumption = 0.0;

    @ApiModelProperty(value = "库存量")
    @Column(precision = 2)
    private Double storage = 0.0;

    @ApiModelProperty(value = "本月实际用量")
    @Column(precision = 2)
    private Double currentMonth = 0.0;

    @ApiModelProperty(value = "本月累计用量")
    @Column(precision = 2)
    private Double totalMonth = 0.0;

    @ApiModelProperty(value = "本月实际单耗")
    @Column(precision = 2)
    private Double currentConsump = 0.0;

    @ApiModelProperty(value = "本月累计单耗")
    @Column(precision = 2)
    private Double totalConsump = 0.0;

    public MaterialConsumptionReportDetail() {
    }

    public MaterialConsumptionReportDetail(Double price, Double planUnitConsumption, Double storage) {
        this.price = price;
        this.planUnitConsumption = planUnitConsumption;
        this.storage = storage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public MaterialConsumptionReportHeader getHeader() {
        return header;
    }

    public void setHeader(MaterialConsumptionReportHeader header) {
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

    public Double getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(Double currentMonth) {
        this.currentMonth = currentMonth;
    }

    public Double getTotalMonth() {
        return totalMonth;
    }

    public void setTotalMonth(Double totalMonth) {
        this.totalMonth = totalMonth;
    }

    public Double getCurrentConsump() {
        return currentConsump;
    }

    public void setCurrentConsump(Double currentConsump) {
        this.currentConsump = currentConsump;
    }

    public Double getTotalConsump() {
        return totalConsump;
    }

    public void setTotalConsump(Double totalConsump) {
        this.totalConsump = totalConsump;
    }
}
