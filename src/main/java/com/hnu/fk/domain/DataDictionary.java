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
@ApiModel(description = "数据字典")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class DataDictionary {
    /**
     * 主键：自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键：自增长")
    private Long id;

    public static final int FLAG_TYPE = -1;
    public static final int FLAG_DATA = 1;

    /**
     * 数据字典编号
     */
    @Column(nullable = false, unique = true)
    @ApiModelProperty("数据字典编号")
    @NotNull(message = "数据字典编号不可为空")
    private Integer dicId;

    /**
     * 字典父编号,字典数据的父编号,父编号为-1
     */
    @Column(nullable = false)
    @ApiModelProperty("字典父编号,字典类型为-1,数据的父编号是其他类型的编号")
    @NotNull(message = "字典父编号不可为空")
    private Integer dicParentId;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    private DataDictionary dataDictionary;

    /**
     * 字典名称
     */
    @Column(nullable = false, unique = true)
    @ApiModelProperty("字典名称")
    @NotNull(message = "字典名称不可为空")
    private String dicName;

    /**
     * 字典值
     */
    @Column(nullable = false, unique = true)
    @ApiModelProperty("字典值")
    @NotNull(message = "字典值不可为空")
    private String dicContent;

    /**
     * 排序
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("显示顺序,自增长")
    private Integer rank = 1;

    /**
     * 字典数据描述
     */
    @ApiModelProperty("字典数据描述")
    private String dicDescription;

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

    public Integer getDicId() {
        return dicId;
    }

    public void setDicId(Integer dicId) {
        this.dicId = dicId;
    }

    public Integer getDicParentId() {
        return dicParentId;
    }

    public void setDicParentId(Integer dicParentId) {
        this.dicParentId = dicParentId;
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getDicDescription() {
        return dicDescription;
    }

    public void setDicDescription(String dicDescription) {
        this.dicDescription = dicDescription;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JsonIgnore
    public DataDictionary getDataDictionary() {
        return dataDictionary;
    }

    public void setDataDictionary(DataDictionary dataDictionary) {
        this.dataDictionary = dataDictionary;
    }
}