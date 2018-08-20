package com.hnu.fk.service;

import com.hnu.fk.domain.Role;
import com.hnu.fk.domain.RoleSecondLevelMenuOperation;
import com.hnu.fk.domain.User;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.UserRepository;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 15:27 2018/8/7
 * @Modified By:
 */
@Service
public class RealmService extends AuthorizingRealm {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 1.从主体传过来的授权信息, 获得用户名
        int id = Integer.parseInt(principals.getPrimaryPrincipal().toString());

        // 2.验证用户
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if(optional.isPresent()){
            user = optional.get();
        } else{
            throw new FkExceptions(EnumExceptions.CHECK_FAILED_USER_NOT_EXISTS);
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 3.查询所有角色
        Set<String> roles = getRolesById(id);
        info.setRoles(roles);

        // 4.查询所有权限
        Set<String> permissions = getPermissionsById(id);
        info.setStringPermissions(permissions);

        return info;
    }

    /**
     * 查询所有的角色
     *
     * @param id
     * @return
     */
    private Set<String> getRolesById(Integer id){
        List<Role> roles = userService.getRolesById(id);
        Set<String> roleSet = new HashSet<>();
        for(Role role : roles){
            roleSet.add(role.getId() + "");
        }
        return roleSet;
    }

    /**
     * 查询所有的权限
     *
     * @param id
     * @return
     */
    private Set<String> getPermissionsById(Integer id){
        List<RoleSecondLevelMenuOperation> permissions = userService.getPermissionsById(id);

        Set<String> permissionSet = new HashSet<>();
        for(RoleSecondLevelMenuOperation permission : permissions){
            permissionSet.add(String.format("%d-%d", permission.getSecondLevelMenuId(), permission.getOperationId()));
        }
        return permissionSet;
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
