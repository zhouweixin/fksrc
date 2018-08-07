package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description: 一级菜单
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@Entity
@Table(name = "permission_first_level_menu")
@ApiModel(description = "一级菜单")
public class FirstLevelMenu {
    /**
     * 编码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键：自增长")
    private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer rank;

    /**
     * 图片存储路径
     */
    @ApiModelProperty("图片存储路径")
    private String path;

    @ApiModelProperty("所在导航的外键")
    @ManyToOne(targetEntity = Navigation.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "navigation_id", referencedColumnName = "id")
    private Navigation navigation;

    @Transient
    @ApiModelProperty("二级菜单")
    private List<SecondLevelMenu> secondLevelMenus = new ArrayList<>();

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

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<SecondLevelMenu> getSecondLevelMenus() {
        return secondLevelMenus;
    }

    public void setSecondLevelMenus(List<SecondLevelMenu> secondLevelMenus) {
        this.secondLevelMenus = secondLevelMenus;
    }
}

