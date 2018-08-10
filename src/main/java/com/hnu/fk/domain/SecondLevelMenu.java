package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description: 二级菜单
 * @Date: Created in 10:20 2018/8/1
 * @Modified By:
 */
@Entity
@Table(name = "permission_second_level_menu")
@ApiModel(description = "二级菜单")
public class SecondLevelMenu {
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
     * 信息
     */
    @ApiModelProperty("图片存储路径")
    private String path;

    /**
     * 页面名称
     */
    @ApiModelProperty("页面名称")
    private String page;

    /**
     * 一级菜单外键
     */
    @ApiModelProperty("一级菜单外键")
    @ManyToOne(targetEntity = FirstLevelMenu.class)
    @JoinColumn(name = "first_level_menu_id", referencedColumnName = "id", nullable = false)
    private FirstLevelMenu firstLevelMenu;

    /**
     * 导航外键
     */
    @ApiModelProperty("导航外键")
    @ManyToOne(targetEntity = Navigation.class)
    @JoinColumn(name = "navigation_id", referencedColumnName = "id", nullable = false)
    private Navigation navigation;

    @Transient
    @ApiModelProperty("操作")
    private List<Operation> operations = new ArrayList<>();

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public FirstLevelMenu getFirstLevelMenu() {
        return firstLevelMenu;
    }

    public void setFirstLevelMenu(FirstLevelMenu firstLevelMenu) {
        this.firstLevelMenu = firstLevelMenu;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
