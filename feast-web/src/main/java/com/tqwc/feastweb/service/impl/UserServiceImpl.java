package com.tqwc.feastweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tqwc.feastcommon.entity.User;
import com.tqwc.feastweb.dto.auth.RegisterRequest;
import com.tqwc.feastweb.dto.auth.UpdateProfileRequest;
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
    public User register(RegisterRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();
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
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(Objects.nonNull(request.getNickname()) && !request.getNickname().isBlank() ? request.getNickname() : username);
        user.setGender(request.getGender());
        user.setAvatar(request.getAvatar());
        user.setBio(request.getBio());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = this.getById(userId);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("用户不存在");
        }

        String newEmail = request.getEmail();
        if (Objects.nonNull(newEmail) && !newEmail.isBlank() && !Objects.equals(newEmail, user.getEmail())) {
            LambdaQueryWrapper<User> emailQuery = new LambdaQueryWrapper<>();
            emailQuery.eq(User::getEmail, newEmail).ne(User::getId, userId);
            if (this.count(emailQuery) > 0) {
                throw new IllegalArgumentException("邮箱已被注册");
            }
            user.setEmail(newEmail);
        }

        if (Objects.nonNull(request.getNickname())) {
            user.setNickname(request.getNickname().isBlank() ? user.getUsername() : request.getNickname());
        }
        if (Objects.nonNull(request.getAvatar())) {
            user.setAvatar(request.getAvatar());
        }

        this.updateById(user);
        return user;
    }
}
