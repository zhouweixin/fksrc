package com.hnu.fk.service;

import com.hnu.fk.domain.Navigation;
import com.hnu.fk.domain.User;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;
import com.hnu.fk.repository.DepartmentRepository;
import com.hnu.fk.repository.UserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RealmService realmService;

    @Autowired
    private DefaultPasswordService defaultPasswordService;



    @Autowired
    private RoleSecondMenuOperationService roleSecondMenuOperationService;

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
        if(user.getDepartment() != null && user.getDepartment().getId() != null && departmentRepository.findById(user.getDepartment().getId()).isPresent() == false){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DEPARTMENT_NOT_EXISTS);
        }

        String password = defaultPasswordService.getDefaultPassword().getDefaultPassword();

        // 随机生成盐
        String salt = UUID.randomUUID().toString();
        // 随机生成加密次数1-5
        int md5Num = new Random().nextInt(5) + 1;
        // 用带盐的加密
        String pwd = new Md5Hash(password, salt, md5Num).toString();

        // 保存盐和密码
        user.setSalt(salt);
        user.setPassword(pwd);
        user.setMd5Num(md5Num);

        return userRepository.save(user);
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
        if (user == null || user.getId() == null || (optional=userRepository.findById(user.getId())).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.UPDATE_FAILED_NOT_EXIST);
        }

        // 验证部门
        if(user.getDepartment() != null && user.getDepartment().getId() != null && departmentRepository.findById(user.getDepartment().getId()).isPresent() == false){
            throw new FkExceptions(EnumExceptions.ADD_FAILED_DEPARTMENT_NOT_EXISTS);
        }

        // 编辑用户的时候不修改密码, 保留以前的不变
        user.setMd5Num(optional.get().getMd5Num());
        user.setSalt(optional.get().getSalt());
        user.setPassword(optional.get().getPassword());

        return userRepository.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {

        // 验证是否存在
        if (userRepository.findById(id).isPresent() == false) {
            throw new FkExceptions(EnumExceptions.DELETE_FAILED_NOT_EXIST);
        }
        userRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteByIdIn(Integer[] ids) {
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
        if(optional.isPresent()){
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

        Pageable pageable =PageRequest.of(page, size, sort);
        return userRepository.findByNameLike("%" + name + "%", pageable);
    }

    /**
     * 登录
     *
     * @param id
     * @param password
     * @return
     */
    public User login(Integer id, String password) {
        // 验证用户是否存在
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if(optional.isPresent()){
            user = optional.get();
        } else {
            throw new FkExceptions(EnumExceptions.LOGIN_FAILED_USER_NOT_EXISTS);
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
        }catch(Exception e){
            // 登录失败
            throw new FkExceptions(EnumExceptions.LOGIN_FAILED_USER_PASSWORD_NOT_MATCHER);
        }

        // 查询用户所有的菜单
        List<Navigation> navigations = roleSecondMenuOperationService.findNavigationsByRoleIds(id);
        user.setNavigations(navigations);

        return user;
    }
}
