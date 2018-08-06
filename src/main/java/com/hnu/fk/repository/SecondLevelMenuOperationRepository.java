package com.hnu.fk.repository;

import com.hnu.fk.domain.Department;
import com.hnu.fk.domain.RoleSecondLevelMenuOperation;
import com.hnu.fk.domain.SecondLevelMenuOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:23 2018/8/6
 * @Modified By:
 */
public interface SecondLevelMenuOperationRepository extends JpaRepository<SecondLevelMenuOperation, Integer> {
}
