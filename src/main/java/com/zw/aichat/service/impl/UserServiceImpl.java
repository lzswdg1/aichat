package com.zw.aichat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zw.aichat.bean.User;
import com.zw.aichat.dao.UserDao;
import com.zw.aichat.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
// Page 和 PageRequest 来自不同的包，这里统一使用 MyBatis-Plus 的分页
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    /**
     * 【已修改】使用 BaseMapper 的 selectById 方法
     */
    @Override
    public User queryById(Long id) {
        return this.userDao.selectById(id);
    }

    /**
     * 【已修改】使用 MyBatis-Plus 的分页查询和 Wrapper
     * 注意：要使用 MyBatis-Plus 的分页功能，需要先配置一个分页插件 (MybatisPlusInterceptor)
     */
    @Override
    public Page<User> queryByPage(User user, PageRequest pageRequest) {
        // 1. 构建查询条件 Wrapper
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(user.getUsername() != null, "username", user.getUsername());
        // wrapper.eq(user.getManager() != null, "manager", user.getManager()); // 根据需要添加更多条件

        // 2. 构建 MyBatis-Plus 的 Page 对象
        Page<User> page = new Page<>(pageRequest.getPageNumber() + 1, pageRequest.getPageSize());

        // 3. 执行分页查询
        return this.userDao.selectPage(page, wrapper);
    }

    /**
     * 【无需修改】insert 方法名和 BaseMapper 中的一致
     */
    @Override
    public int insert(User user) {
        return this.userDao.insert(user);
    }

    /**
     * 【已修改】使用 BaseMapper 的 updateById 方法
     */
    @Override
    public int update(User user) {
        return this.userDao.updateById(user);
    }

    /**
     * 【已修改】BaseMapper 的 deleteById 返回的也是影响行数
     */
    @Override
    public boolean deleteById(Long id) {
        return this.userDao.deleteById(id) > 0;
    }

    /**
     * 【已修改】使用 Wrapper 构建登录查询条件
     * 警告：在真实项目中，密码不应以明文方式存储和比较！
     */
    @Override
    public Long doLogin(User user) {
        // 1. 构建查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        // wrapper.eq("password", user.getPassword()); // 实际项目中密码需要加密处理

        // 2. 执行查询，selectOne 用于查询单个结果
        User foundUser = this.userDao.selectOne(wrapper);

        // 3. 返回 ID
        return (foundUser != null) ? foundUser.getId() : null;
    }

    /**
     * 【无需修改】因为 insert 方法名一致
     */
    @Override
    public void doRegister(User user) {
        this.userDao.insert(user);
    }
}