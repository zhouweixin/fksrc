package com.hnu.fk.service;

import com.hnu.fk.domain.LoginLog;
import com.hnu.fk.repository.LoginLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/7
 */
@Service
public class LoginLogService {
    @Autowired
    private LoginLogRepository loginLogRepository;

    /**
     * 新增
     */
    public LoginLog add(LoginLog loginLog){
        return loginLogRepository.save(loginLog);
    }

    /**
     * 查询所有日志-分页
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<LoginLog> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            LoginLog.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            // 如果不存在就设置为id
            sortFieldName = "id";
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return loginLogRepository.findAll(pageable);
    }

    /**
     * 通过日期模糊查询
     * @param date
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<LoginLog> findByDateLike(String date, Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            LoginLog.class.getDeclaredField(sortFieldName);
        } catch (Exception e) {
            // 如果不存在就设置为id
            sortFieldName = "id";
        }

        Sort sort = null;
        if (asc == 0) {
            sort = new Sort(Sort.Direction.DESC, sortFieldName);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortFieldName);
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return loginLogRepository.findByTimeLike(date,pageable);
    }
}
