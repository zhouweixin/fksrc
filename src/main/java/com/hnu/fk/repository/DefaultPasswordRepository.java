package com.hnu.fk.repository;

import com.hnu.fk.domain.DefaultPassword;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 说明: 默认密码处理接口类
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
public interface DefaultPasswordRepository extends JpaRepository<DefaultPassword, Integer> {

}
