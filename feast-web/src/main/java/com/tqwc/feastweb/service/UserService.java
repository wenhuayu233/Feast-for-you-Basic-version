package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tqwc.feastweb.dto.auth.RegisterRequest;
import com.tqwc.feastweb.dto.auth.UpdateProfileRequest;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface UserService extends IService<User> {
    User register(RegisterRequest request);
    User login(String account, String rawPassword);
    User updateProfile(Long userId, UpdateProfileRequest request);
}
