package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface UserService extends IService<User> {
    User register(String username, String email, String rawPassword);
    User login(String account, String rawPassword);
}
