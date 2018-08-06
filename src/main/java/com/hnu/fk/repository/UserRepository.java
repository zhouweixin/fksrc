package com.hnu.fk.repository;

import com.hnu.fk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:23 2018/8/6
 * @Modified By:
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
