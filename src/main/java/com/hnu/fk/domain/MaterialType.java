package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 物料类型
 * @Date: Created in 16:33 2018/8/20
 * @Modified By:
 */
@Entity
@Table(name = "produce_material_type")
@ApiModel(value = "物料类型")
public class MaterialType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键: 自增长")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    public MaterialType() {
    }

    public MaterialType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
