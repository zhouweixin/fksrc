package com.hnu.fk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author: zhouweixin
 * @Description: 登录配置
 * @Date: Created in 15:21 2018/8/14
 * @Modified By:
 */
@Configuration
public class FkSecurityConfig implements WebMvcConfigurer {
    public final static String SESSION_USER = "user";

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        // 登录不拦截
        addInterceptor.excludePathPatterns("/login.html");
        addInterceptor.excludePathPatterns("/swagger-ui**");
        addInterceptor.excludePathPatterns("/docs**");
        addInterceptor.excludePathPatterns("/css**");
        addInterceptor.excludePathPatterns("/js**");

        // 拦截所有
//        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession();

            // 判断是否已有该用户登录的session
            if (session.getAttribute(SESSION_USER) != null) {
                return true;
            }

//            System.out.println("============================");
//            System.out.println("重定向到登录页");
//            System.out.println("============================");

//            String url = "/fk/login.html";
//            response.sendRedirect(url);
            return true;
        }
    }
}
