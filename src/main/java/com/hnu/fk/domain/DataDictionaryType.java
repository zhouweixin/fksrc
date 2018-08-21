package com.hnu.fk.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "basicinfo_dictionary_type")
@ApiModel(description = "数据字典类型")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class DataDictionaryType {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键：自增长")
    private Long id;

    /**
     * 字典类型名称
     */
    @Column(nullable = false)
    @ApiModelProperty("字典类型名称")
    @NotNull(message = "字典类型名称不可为空")
    private String typeName;

    /**
     * 字典类型值
     */
    @Column(nullable = false,unique = true)
    @ApiModelProperty("字典类型值,唯一不为空")
    private String typeValue;


    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer rank;

    /**
     *
     */
    @ApiModelProperty("字典数据")
    @OneToMany(targetEntity = DataDictionary.class,cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "parent_type_id", referencedColumnName = "id")
    private Set<DataDictionary> dataDictionaries;

    /**
     * 更新时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间,自动生成")
    private Date updateTime = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
