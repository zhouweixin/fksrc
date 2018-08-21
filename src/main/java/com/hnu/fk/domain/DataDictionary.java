package com.hnu.fk.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: huXuDong
 * @Description: 数据字典
 * @Date: Created in 10:20 2018/8/15
 * @Modified By:
 */
@Entity
@Table(name = "basicinfo_dictionary")
@ApiModel(description = "数据字典数据")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class DataDictionary {
    /**
     * 主键：自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键：自增长")
    private Long id;

    /**
     * 字典名称
     */
    @Column(nullable = false)
    @ApiModelProperty("字典名称")
    @NotNull(message = "字典名称不可为空")
    private String dicName;

    /**
     * 字典值
     */
    @Column(nullable = false)
    @ApiModelProperty("字典值")
    @NotNull(message = "字典值不可为空")
    private String dicContent;


    /**
     * 字典数据描述
     */
    @ApiModelProperty("字典数据描述")
    private String dicDescription;

    /**
     * 排序
     */
    @ApiModelProperty("显示顺序,自增长")
    private Integer rank;

    /**
     * 字典所属类型
     */
    @ApiModelProperty("字典所属类型")
    @ManyToOne(targetEntity = DataDictionaryType.class)
    @JoinColumn(name = "parent_type_id", referencedColumnName = "id")
    private DataDictionaryType dataDictionaryType;

    /**
     * 更新时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)//注解时间戳
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//注解时间格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间,自动生成")
    private Date updateTime = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicContent() {
        return dicContent;
    }

    public void setDicContent(String dicContent) {
        this.dicContent = dicContent;
    }

    public String getDicDescription() {
        return dicDescription;
    }

    public void setDicDescription(String dicDescription) {
        this.dicDescription = dicDescription;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @JsonIgnore
    public DataDictionaryType getDataDictionaryType() {
        return dataDictionaryType;
    }

    public void setDataDictionaryType(DataDictionaryType dataDictionaryType) {
        this.dataDictionaryType = dataDictionaryType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}