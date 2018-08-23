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
    @ApiModelProperty(value = "物料消耗统计表头")
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
    private Double planUnitConsump = 0.0;

    @ApiModelProperty(value = "库存量")
    @Column(precision = 2)
    private Double storage = 0.0;

    @ApiModelProperty(value = "本月实际用量")
    @Column(precision = 2)
    private Double curConsump = 0.0;

    @ApiModelProperty(value = "本月累计用量")
    @Column(precision = 2)
    private Double totalConsump = 0.0;

    @ApiModelProperty(value = "本月实际单耗")
    @Column(precision = 2)
    private Double curUnitConsump = 0.0;

    @ApiModelProperty(value = "本月累计单耗")
    @Column(precision = 2)
    private Double totalUnitConsump = 0.0;

    /**
     * 无参构造函数
     */
    public MaterialConsumptionReportDetail() {
    }

    /**
     * 有参构造函数
     *
     * @param price
     * @param planUnitConsump
     * @param storage
     */
    public MaterialConsumptionReportDetail(Double price, Double planUnitConsump, Double storage) {
        this.price = price;
        this.planUnitConsump = planUnitConsump;
        this.storage = storage;
    }

    /**
     * 设置本月实际用量和本月实际单耗
     *
     * @param currentMonth
     * @param curRawOre
     */
    public void setCurrentMonthAndCurConsump(double currentMonth, double curRawOre) {
        this.curConsump = currentMonth;
        if(curRawOre > 0){
            // t转kg
            this.curUnitConsump = 1000 * currentMonth / curRawOre;
        }
    }

    /**
     * 设置累计实际用量和累计单耗
     *
     * @param totalMonth
     * @param totalRawOre
     */
    public void setTotalMonth(double totalMonth, double totalRawOre) {
        this.totalConsump = totalMonth;
        if(totalRawOre > 0){
            // t转kg
            this.totalUnitConsump = 1000 * totalMonth / totalRawOre;
        }
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

    public Double getPlanUnitConsump() {
        return planUnitConsump;
    }

    public void setPlanUnitConsump(Double planUnitConsump) {
        this.planUnitConsump = planUnitConsump;
    }

    public Double getStorage() {
        return storage;
    }

    public void setStorage(Double storage) {
        this.storage = storage;
    }

    public Double getCurConsump() {
        return curConsump;
    }

    public void setCurConsump(Double curConsump) {
        this.curConsump = curConsump;
    }

    public Double getTotalConsump() {
        return totalConsump;
    }

    public void setTotalConsump(Double totalConsump) {
        this.totalConsump = totalConsump;
    }

    public Double getCurUnitConsump() {
        return curUnitConsump;
    }

    public void setCurUnitConsump(Double curUnitConsump) {
        this.curUnitConsump = curUnitConsump;
    }

    public Double getTotalUnitConsump() {
        return totalUnitConsump;
    }

    public void setTotalUnitConsump(Double totalUnitConsump) {
        this.totalUnitConsump = totalUnitConsump;
    }
}
