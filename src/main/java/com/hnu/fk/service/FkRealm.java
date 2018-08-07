package com.hnu.fk.service;

import com.hnu.fk.domain.User;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.UserRepository;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 15:27 2018/8/7
 * @Modified By:
 */
@Service
public class FkRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository userRepository;

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1.从主体传过来的认证信息中, 获得用户名
        Integer id = Integer.parseInt(token.getPrincipal().toString());

        // 2.通过用户名去到数据库中获取凭证
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if(optional.isPresent()){
            user = optional.get();
        } else{
            throw new FkExceptions(EnumExceptions.LOGIN_FAILED_USER_NOT_EXISTS);
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(id, user.getPassword(), "fkRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
        return simpleAuthenticationInfo;
    }
}
