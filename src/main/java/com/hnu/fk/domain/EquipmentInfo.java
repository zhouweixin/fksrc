package com.hnu.fk.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
@Entity
@Table(name = "produce_equipment_info")
@ApiModel(description = "设备信息")
@ExcelTarget("equipmentInfo")
public class EquipmentInfo {
    @Id
    @ApiModelProperty("工段ID，主键: 自增长")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称_msEquipment",orderNum = "9",width = 15,isImportField = "true_msEquipment")
    private String name;

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

    @Override
    public String toString() {
        return "EquipmentInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
