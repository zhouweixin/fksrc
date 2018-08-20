package com.hnu.fk.service;

import com.hnu.fk.domain.*;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.*;
import com.hnu.fk.utils.ActionLogUtil;
import com.hnu.fk.utils.LoginLogUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:28 2018/8/6
 * @Modified By:
 */
@Service
public class UserService {
    
    public static final String NAME = "用户";
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RealmService realmService;

    @Autowired
    private DefaultPasswordService defaultPasswordService;

    @Autowired
    private RoleSecondLevelMenuOperationRepository roleSecondLevelMenuOperationRepository;

    @Autowired
    private RoleSecondMenuOperationService roleSecondMenuOperationService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NavigationService navigationService;

    /**
     * 新增
     *
     * @param user
     * @return
     */
    public User save(User user) {

        // 验证是否存在
        if (user == null || (user.getId() != null && userRepository.findById(user.getId()).isPresent()) == true) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DUPLICATE);
        }

        // 验证部门
        if (user.getDepartment() != null && user.getDepartment().getId() != null && departmentRepository.findById(user.getDepartment().getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DEPARTMENT_NOT_EXISTS);
        }

        // 获得默认密码
        String password = defaultPasswordService.getDefaultPassword().toString();
        // 加密
        encryptPassword(user, password);

        User save = userRepository.save(user);
        ActionLogUtil.log(NAME, 0, save);
        return save;
    }

    /**
     * 更新
     *
     * @param user
     * @return
     */
    public User update(User user) {

        // 验证是否存在
        Optional<User> optional = null;
        if (user == null || user.getId() == null || (optional = userRepository.findById(user.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        // 验证部门
        if (user.getDepartment() != null && user.getDepartment().getId() != null && departmentRepository.findById(user.getDepartment().getId()).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DEPARTMENT_NOT_EXISTS);
        }

        // 编辑用户的时候不修改密码, 保留以前的不变
        user.setMd5Num(optional.get().getMd5Num());
        user.setSalt(optional.get().getSalt());
        user.setPassword(optional.get().getPassword());

        User oldUser = optional.get();
        User newUser = userRepository.save(user);
        ActionLogUtil.log(NAME, oldUser, newUser);
        return newUser;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {

        // 验证是否存在
        Optional<User> optional = null;
        if ((optional=userRepository.findById(id)).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }

        ActionLogUtil.log(NAME, 1, optional.get());
        userRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
        ActionLogUtil.log(NAME, 1, roleRepository.findByIdInAndFlag(Arrays.asList(ids), 0));
        userRepository.deleteByIdIn(Arrays.asList(ids));
    }

    /**
     * 通过编码查询
     *
     * @param id
     * @return
     */
    public User findOne(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 查询所有-分页
     *
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<User> findAllByPage(Integer page, Integer size, String sortFieldName, Integer asc) {

        // 判断排序字段名是否存在
        try {
            User.class.getDeclaredField(sortFieldName);
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
        return userRepository.findAll(pageable);
    }

    /**
     * 通过名称模糊分页查询
     *
     * @param name
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<User> findByNameLikeByPage(String name, Integer page, Integer size, String sortFieldName,
                                           Integer asc) {

        // 判断排序字段名是否存在
        try {
            User.class.getDeclaredField(sortFieldName);
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
        return userRepository.findByNameLike("%" + name + "%", pageable);
    }

    /**
     * 登录
     *
     * @param id
     * @param password
     * @return
     */
    public User login(Integer id, String password, HttpSession session) {
        // 验证用户是否存在
        if (!userRepository.findById(id).isPresent()) {
            throw new FkExceptions(EnumExceptions.LOGIN_FAILED_USER_NOT_EXISTS);
        }

        // 登录验证
        User user = null;
        if ((user=this.checkUserPasswordForLogin(id, password)) == null) {
            throw new FkExceptions(EnumExceptions.LOGIN_FAILED_USER_PASSWORD_NOT_MATCHER);
        }

//        session.setAttribute(FkSecurityConfig.SESSION_USER, user);

        return user;
    }

    /**
     * 密码加密
     *
     * @return
     */
    private boolean encryptPassword(User user, String password) {
        if (user == null || password == null) {
            return false;
        }

        // 随机生成盐
        String salt = UUID.randomUUID().toString();
        // 随机生成加密次数1-5
        int md5Num = new Random().nextInt(5) + 1;
        // 用带盐的加密
        String pwd = new Md5Hash(password, salt, md5Num).toString();

        user.setSalt(salt);
        user.setMd5Num(md5Num);
        user.setPassword(pwd);
        return true;
    }

    /**
     * 校验用户密码-用于登录
     *
     * @param id
     * @param password
     */
    private User checkUserPasswordForLogin(Integer id, String password) {
        // 验证用户是否存在
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new FkExceptions(EnumExceptions.CHECK_PASSWORD_FAILED_NOT_EXISTS);
        }

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // md5 加密
        matcher.setHashAlgorithmName("md5");
        // 迭代次数
        matcher.setHashIterations(user.getMd5Num());

        // 1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realmService);
        realmService.setCredentialsMatcher(matcher);

        // 2、主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(id + "", password);

        try {
            // 认证登录
            subject.login(token);
        } catch (Exception e) {
            // 登录失败
            return null;
        }

        // 如果验证通过
        if (subject.isAuthenticated()) {
            // 查询用户所有的菜单
            List<Navigation> navigations = navigationService.findAllByUserId(id);
            user.setNavigations(navigations);

            // 获取 session， 如果不存在就创建
            Session session = subject.getSession(true);
            session.setAttribute("user", user);

            // 保存登录日志
            LoginLogUtil.log();

            return user;
        }

        return null;
    }

    /**
     * 校验用户密码
     *
     * @param id
     * @param password
     */
    private boolean checkUserPassword(Integer id, String password) {
        // 验证用户是否存在
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new FkExceptions(EnumExceptions.CHECK_PASSWORD_FAILED_NOT_EXISTS);
        }

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // md5 加密
        matcher.setHashAlgorithmName("md5");
        // 迭代次数
        matcher.setHashIterations(user.getMd5Num());

        // 1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realmService);
        realmService.setCredentialsMatcher(matcher);

        // 2、主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(id + "", password);

        try {
            // 认证登录
            subject.login(token);
        } catch (Exception e) {
            // 登录失败
            return false;
        }

        // 如果验证通过
        if (subject.isAuthenticated()) {
            return true;
        }

        return false;
    }

    /**
     * 重置密码
     *
     * @param id
     */
    @Transactional
    public void resetPassword(Integer id) {
        // 验证是否存在
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent() == false) {
            throw new FkExceptions(EnumExceptions.RESET_PASSWORD_FAILED_NOT_EXIST);
        }
        User user = optional.get();

        // 查询默认密码
        String defaultPassword = defaultPasswordService.getDefaultPassword().getPassword();

        // 加密
        encryptPassword(user, defaultPassword);

        // 修改
        userRepository.save(user);
    }

    /**
     * 修改密码
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     * @param reNewPassword
     */
    public void updatePassword(Integer id, String oldPassword, String newPassword, String reNewPassword) {
        // 验证用户是否存在
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new FkExceptions(EnumExceptions.UPDATE_PASSWORD_FAILED_USER_NOT_EIXSTS);
        }

        // 校验新旧密码的合法性
        if (newPassword == null || "".equals(newPassword)) {
            throw new FkExceptions(EnumExceptions.UPDATE_PASSWORD_FAILED_NEW_PASSWORD_NULL);
        } else if (newPassword.length() < 6) {
            throw new FkExceptions(EnumExceptions.UPDATE_PASSWORD_FAILED_LEN_LESS_SIX);
        } else if (!newPassword.equals(reNewPassword)) {
            throw new FkExceptions(EnumExceptions.UPDATE_PASSWORD_FAILED_TWICE_PASSWORD_DIFF);
        }

        // 校验密码
        if (this.checkUserPassword(id, oldPassword) == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_PASSWORD_FAILED_CHECK_FAILED);
        }

        // 加密
        encryptPassword(user, newPassword);

        // 修改
        userRepository.save(user);
    }

    /**
     * 通过部门查询所有员工-分页
     *
     * @param departmentId
     * @param page
     * @param size
     * @param sortFieldName
     * @param asc
     * @return
     */
    public Page<User> findByDepartment(Integer departmentId, Integer page, Integer size, String sortFieldName, Integer asc) {
        Optional<Department> optional = departmentRepository.findById(departmentId);
        if (optional.isPresent() == false) {
            throw new FkExceptions(EnumExceptions.QUERY_FAILED_DEPARTMENT_NOT_EXISTS);
        }

        // 查询部门的子部门
        List<Department> departments = departmentService.findSonAndSelfByParentDepartment(optional.get());

        // 判断排序字段名是否存在
        try {
            User.class.getDeclaredField(sortFieldName);
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
        return userRepository.findByDepartmentIn(departments, pageable);
    }

    /**
     * 给用户分配角色
     *
     * @param userIds
     * @param roleIds
     */
    @Transactional
    public void assignRolesToUsers(Integer[] userIds, Integer[] roleIds) {
        // 删除用户所有的角色
        ActionLogUtil.log(NAME, 1, userRoleRepository.findByUserIdIn(Arrays.asList(userIds)));
        userRoleRepository.deleteByUserIdIn(Arrays.asList(userIds));

        // 重新分配角色
        List<UserRole> userRoles = new ArrayList<>();

        List<User> users = userRepository.findAllById(Arrays.asList(userIds));
        List<Role> roles = roleRepository.findAllById(Arrays.asList(roleIds));
        for (User user : users) {
            for (Role role : roles) {
                userRoles.add(new UserRole(user.getId(), role.getId()));
            }
        }

        List<UserRole> userRoleList = userRoleRepository.saveAll(userRoles);
        ActionLogUtil.log(NAME, 0, userRoleList);
    }

    /**
     * 查询用户的角色
     *
     * @param id
     * @return
     */
    public List<Role> getRolesById(Integer id) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(id);
        Set<Integer> roleIds = new HashSet<>();
        for (UserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }

        return roleRepository.findAllById(roleIds);
    }

    /**
     * 查询用户的权限
     *
     * @param id
     * @return
     */
    public List<RoleSecondLevelMenuOperation> getPermissionsById(Integer id) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(id);
        Set<Integer> roleIds = new HashSet<>();
        for (UserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }
        return roleSecondLevelMenuOperationRepository.findByRoleIdIn(roleIds);
    }

    /**
     * 更新部门
     *
     * @param id
     * @param departmentId
     */
    @Transactional
    public void updateDepartmentById(Integer id, Integer departmentId) {
        if(userRepository.findById(id).isPresent() == false){
            throw new FkExceptions(EnumExceptions.UPDATE_DEPARTMENT_FAILED_USER_NOT_EXISTS);
        }

        Optional<Department> optional = departmentRepository.findById(departmentId);
        if(optional.isPresent() ==  false){
            throw new FkExceptions(EnumExceptions.UPDATE_DEPARTMENT_FAILED_DEPARTMENT_NOT_EXISTS);
        }

        userRepository.updateDepartmentById(optional.get(), id);
    }

    /**
     * 更新启用
     *
     * @param id
     * @param enable
     */
    @Transactional
    public void updateEnableById(Integer id, Integer enable) {
        if(userRepository.findById(id).isPresent() == false){
            throw new FkExceptions(EnumExceptions.UPDATE_ENABLE_FAILED_USER_EXISTS);
        }

        userRepository.updateEnableById(enable, id);
    }
}
