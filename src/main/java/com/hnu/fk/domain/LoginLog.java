package com.hnu.fk.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zhouweixin
 * @Description: 登录日志
 * @Date: Created in 14:29 2018/8/6
 * @Modified By:
 */
@Entity
@Table(name = "permission_login_log")
@ApiModel(description = "登录日志")
public class LoginLog {
    @Id
    @ApiModelProperty("主键: 自增长")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "序号", width = 10 ,orderNum = "0")
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ApiModelProperty("用户")
    @ExcelEntity(id = "Operator")
    private User user;

    @ApiModelProperty("ip地址")
    @Excel(name = "ip地址",width = 15 ,orderNum = "2")
    private String ipAddress;

    @ApiModelProperty("地址")
    @Excel(name = "地址",width = 20 ,orderNum = "3")
    private String address;

    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("时间")
    @Excel(name = "时间",width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss" ,orderNum = "4")
    private Date time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
