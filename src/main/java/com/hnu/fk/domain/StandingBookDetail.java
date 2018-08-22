package com.hnu.fk.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "produce_standing_book_detail")
@ApiModel(description ="调度台账明细" )
public class StandingBookDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键:自增长")
    private Integer id;

    @ApiModelProperty("外键:表头id")
    @ManyToOne(targetEntity = StandingBookHead.class)
    @JoinColumn(name = "head_id",referencedColumnName = "id")
    private StandingBookHead standingBookHead;

    @ApiModelProperty("外键:项目id")
    @ManyToOne(targetEntity = StandingBookItem.class)
    @JoinColumn(name = "item_id",referencedColumnName = "id")
    private StandingBookItem standingBookItem;

    @ApiModelProperty("字段类型1:浮点型 2:字符型 3:日期型")
    @Column(nullable = false)
    @NotNull(message = "字段类型不能为空")
    private Integer fieldId;

    @ApiModelProperty("项目值")
    private String ItemValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StandingBookHead getStandingBookHead() {
        return standingBookHead;
    }

    public void setStandingBookHead(StandingBookHead standingBookHead) {
        this.standingBookHead = standingBookHead;
    }

    public StandingBookItem getStandingBookItem() {
        return standingBookItem;
    }

    public void setStandingBookItem(StandingBookItem standingBookItem) {
        this.standingBookItem = standingBookItem;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getItemValue() {
        return ItemValue;
    }

    public void setItemValue(String itemValue) {
        ItemValue = itemValue;
    }
}
