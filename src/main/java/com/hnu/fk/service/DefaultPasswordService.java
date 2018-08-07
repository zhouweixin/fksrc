package com.hnu.fk.service;

import com.hnu.fk.domain.DefaultPassword;
import com.hnu.fk.repository.DefaultPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 说明:
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

@Service
public class DefaultPasswordService {

    //默认密码
    private static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private DefaultPasswordRepository defaultPasswordRepository;


    /**
     * 获取默认密码
     *
     * @return DefaultPassword
     */
    public DefaultPassword getDefaultPassword() {
        Optional<DefaultPassword> option = defaultPasswordRepository.findById(1);
        if (!option.isPresent()) {
            DefaultPassword defaultPassword = new DefaultPassword(1, DEFAULT_PASSWORD);
            defaultPasswordRepository.save(defaultPassword);
            option = defaultPasswordRepository.findById(1);
        }
        return option.get();
    }

    /**
     * 更新默认密码
     *
     * @param password String
     * @return DefaultPassword
     */
    public DefaultPassword updateDefaultPassword(String password) {
        DefaultPassword defaultPassword = new DefaultPassword(1, password);
        return defaultPasswordRepository.save(defaultPassword);
    }
}
