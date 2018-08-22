package com.hnu.fk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@ApiModel(description = "台账项目类别")
@Table(name = "produce_standing_book_type")
public class StandingBookType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键:自增长")
    private Integer id;

    @ApiModelProperty("项目类别名称")
    @Column(nullable = false)
    @NotNull(message = "项目类别名称不可为空")
    private String itemTypeName;

    @ApiModelProperty("外键:所属工段Id")
    @ManyToOne(targetEntity = SectionInfo.class)
    @JoinColumn(name = "section_id",referencedColumnName = "id")
    private SectionInfo sectionInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    @JsonIgnore
    public SectionInfo getSectionInfo() {
        return sectionInfo;
    }

    public void setSectionInfo(SectionInfo sectionInfo) {
        this.sectionInfo = sectionInfo;
    }
}
