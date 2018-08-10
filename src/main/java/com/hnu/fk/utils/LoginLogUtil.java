package com.hnu.fk.utils;

import com.hnu.fk.domain.LoginLog;
import com.hnu.fk.domain.User;
import com.hnu.fk.repository.LoginLogRepository;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * 说明:登录日志工具类
 *
 * @author WaveLee
 * 日期: 2018/8/7
 */
@Component
public class LoginLogUtil {
    private static LoginLogRepository loginLogRepository;

    @Autowired
    public LoginLogUtil(LoginLogRepository loginLogRepository) {
        LoginLogUtil.loginLogRepository = loginLogRepository;
    }

    public static void log(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Date currentTime = new Date();

        User user = (User)SecurityUtils.getSubject().getSession(true).getAttribute("user");
        LoginLog loginLog = new LoginLog();
        if (user != null) {
            loginLog.setUser(user);
        }
        loginLog.setTime(currentTime);
        loginLog.setIpAddress(IpUtil.getIpAddr(request));
        loginLog.setAddress(IpToAddressUtil.getAddresses(IpUtil.getIpAddr(request)));

        loginLogRepository.save(loginLog);
    }


}
