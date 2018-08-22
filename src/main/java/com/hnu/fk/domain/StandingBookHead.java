package com.hnu.fk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@ApiModel(description = "调度台账表头")
@Table(name = "produce_standing_book_head")
public class StandingBookHead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键:自增长")
    private Integer id;

    @ApiModelProperty("调度台账编号")
    @Column(nullable = false)
    @NotNull(message = "调度台账编号不可为空")
    private String standingBook;

    @ApiModelProperty("值班调度员")
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="permission_user",referencedColumnName = "id")
    private User user;

    @ApiModelProperty("所属班组")
    @ManyToOne(targetEntity = DataDictionary.class)
    @JoinColumn(name = "basicinfo_dictionary",referencedColumnName = "id")
    private DataDictionary dataDictionary;

    @ApiModelProperty("备注")
    private String ssbz;

    @ApiModelProperty("调度台账日期")
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date = new Date();

    @ApiModelProperty("台账录入时间")
    @Temporal(value = TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date time = new Date();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStandingBook() {
        return standingBook;
    }

    public void setStandingBook(String standingBook) {
        this.standingBook = standingBook;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public DataDictionary getDataDictionary() {
        return dataDictionary;
    }

    public void setDataDictionary(DataDictionary dataDictionary) {
        this.dataDictionary = dataDictionary;
    }

    public String getSsbz() {
        return ssbz;
    }

    public void setSsbz(String ssbz) {
        this.ssbz = ssbz;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
