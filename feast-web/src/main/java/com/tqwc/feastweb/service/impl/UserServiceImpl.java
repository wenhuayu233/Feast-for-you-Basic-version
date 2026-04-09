package com.tqwc.feastweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tqwc.feastcommon.entity.User;
import com.tqwc.feastweb.mapper.UserMapper;
import com.tqwc.feastweb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(String username, String email, String rawPassword) {
        LambdaQueryWrapper<User> userNameQuery = new LambdaQueryWrapper<>();
        userNameQuery.eq(User::getUsername, username);
        if (this.count(userNameQuery) > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }

        LambdaQueryWrapper<User> emailQuery = new LambdaQueryWrapper<>();
        emailQuery.eq(User::getEmail, email);
        if (this.count(emailQuery) > 0) {
            throw new IllegalArgumentException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setNickname(username);
        user.setStatus(1);
        this.save(user);
        return user;
    }

    @Override
    public User login(String account, String rawPassword) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (account.contains("@")) {
            queryWrapper.eq(User::getEmail, account);
        } else {
            queryWrapper.eq(User::getUsername, account);
        }
        User user = this.getOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("账号或密码错误");
        }
        if (!Objects.equals(user.getStatus(), 1)) {
            throw new IllegalStateException("用户已被禁用");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("账号或密码错误");
        }
        return user;
    }
}
