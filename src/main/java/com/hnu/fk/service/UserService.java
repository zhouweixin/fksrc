package com.hnu.fk.service;

import com.hnu.fk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:28 2018/8/6
 * @Modified By:
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


}
