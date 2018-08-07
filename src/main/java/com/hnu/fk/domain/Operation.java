package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 操作:1浏览;2新增;3查询;4删除;5编辑;6审核
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@Entity
@Table(name = "permission_operation")
@ApiModel(description = "操作")
public class Operation {
	/** 编码 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键:自增长")
	private Integer id;

	/** 名称 */
    @ApiModelProperty("名称")
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
		return "Operation [id=" + id + ", name=" + name + "]";
	}

}
