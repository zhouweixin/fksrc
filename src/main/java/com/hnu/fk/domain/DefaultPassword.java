package com.hnu.fk.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 说明: 默认密码实体类
 * <br>
 *
 * @author ZSCDumin
 * <br>
 * 邮箱: 2712220318@qq.com
 * <br>
 * 日期: 2018/8/7
 * <br>
 * 版本: 1.0
 */

@Entity
@Table(name = "basicinfo_default_password")
@ApiModel(description = "默认密码")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultPassword {

    /**
     * 主键:自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键:自增长", name = "id", example = "1")
    private Integer id;

    /**
     * 默认密码
     */
    @ApiModelProperty(value = "默认密码", name = "password", example = "123456")
    private String password;
}
