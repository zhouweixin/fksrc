package com.hnu.fk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗统计类型表
 * @Date: Created in 10:00 2018/8/23
 * @Modified By:
 */
@Entity
@Table(name = "produce_material_consumption_report_type_detail")
@ApiModel(value = "物料消耗统计类型表")
public class MaterialConsumptionReportTypeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键: 自增长")
    private Long id;

    @ManyToOne(targetEntity = MaterialConsumptionReportHeader.class)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    @ApiModelProperty(value = "物料消耗统计表头")
    private MaterialConsumptionReportHeader header;

    @ManyToOne(targetEntity = MaterialType.class)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ApiModelProperty(value = "物料消耗项目")
    private MaterialType type;

    @ApiModelProperty(value = "本月成本")
    @Column(precision = 2)
    private Double curCost = 0.0;

    @ApiModelProperty(value = "累计成本")
    @Column(precision = 2)
    private Double totalCost = 0.0;

    @ApiModelProperty(value = "本月单位成本")
    @Column(precision = 2)
    private Double curUnitCost = 0.0;

    @ApiModelProperty(value = "累计单位成本")
    @Column(precision = 2)
    private Double totalUnitCost = 0.0;

    /**
     * 默认构造函数
     */
    public MaterialConsumptionReportTypeDetail() {
    }

    /**
     * 有参构造函数
     * @param type
     */
    public MaterialConsumptionReportTypeDetail(MaterialType type) {
        this.type = type;
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

    public MaterialType getType() {
        return type;
    }

    public void setType(MaterialType type) {
        this.type = type;
    }

    public Double getCurCost() {
        return curCost;
    }

    public void setCurCost(Double curCost) {
        this.curCost = curCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getCurUnitCost() {
        return curUnitCost;
    }

    public void setCurUnitCost(Double curUnitCost) {
        this.curUnitCost = curUnitCost;
    }

    public Double getTotalUnitCost() {
        return totalUnitCost;
    }

    public void setTotalUnitCost(Double totalUnitCost) {
        this.totalUnitCost = totalUnitCost;
    }

    /**
     * 设置本月成本, 本月单位成本
     *
     * @param curCost
     */
    public void setCurCostAndCurUnitCost(double curCost, double curRawOre) {
        this.curCost = curCost;
        if(curRawOre > 0) {
            this.curUnitCost = curCost / curRawOre;
        }
    }

    /**
     * 设置累计成本, 累计单位成本
     *
     * @param totalCost
     */
    public void setTotalCostAndTotalUnitCost(double totalCost, double totalRawOre) {
        this.totalCost = totalCost;
        if(totalRawOre > 0){
            this.totalUnitCost = totalCost / totalRawOre;
        }
    }
}
