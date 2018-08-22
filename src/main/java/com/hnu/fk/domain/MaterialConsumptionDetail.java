package com.hnu.fk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 物料消耗明细表
 * @Date: Created in 9:36 2018/8/21
 * @Modified By:
 */
@Entity
@Table(name = "produce_material_consumption_detail")
@ApiModel(value = "物料消耗明细")
public class MaterialConsumptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键: 自增长")
    private Long id;

    @ManyToOne(targetEntity = MaterialConsumptionHeader.class)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    @ApiModelProperty(value = "物料消耗表头")
    private MaterialConsumptionHeader header;

    @ManyToOne(targetEntity = MaterialConsumptionItem.class)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ApiModelProperty(value = "物料消耗项目")
    private MaterialConsumptionItem item;

    @Column(precision = 2)
    @ApiModelProperty(value = "物料消耗项目值")
    private Double value = 0.0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public MaterialConsumptionHeader getHeader() {
        return header;
    }

    public void setHeader(MaterialConsumptionHeader header) {
        this.header = header;
    }

    public MaterialConsumptionItem getItem() {
        return item;
    }

    public void setItem(MaterialConsumptionItem item) {
        this.item = item;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
