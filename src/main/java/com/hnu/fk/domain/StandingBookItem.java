package com.hnu.fk.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@ApiModel(description = "调度台账项目")
@Table(name = "produce_standing_book_item")
public class StandingBookItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键:自增长")
    private Integer id;

    @ApiModelProperty("外键:台账项目类别Id")
    @ManyToOne(targetEntity = StandingBookType.class)
    @JoinColumn(name = "booktype_id",referencedColumnName = "id")
    private StandingBookType standingBookType;

    @ApiModelProperty("字段类型1:浮点型 2:字符型 3:日期型")
    @Column(nullable = false)
    @NotNull(message = "字段类型不可以为空")
    @Range(min = 1,max = 3)
    private Integer fieldId;

    @ApiModelProperty("台账项目名称")
    @Column(nullable = false)
    @NotNull(message = "项目名称不可以为空")
    private String itemName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StandingBookType getStandingBookType() {
        return standingBookType;
    }

    public void setStandingBookType(StandingBookType standingBookType) {
        this.standingBookType = standingBookType;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
