package com.tqwc.feastweb.service.impl;

import com.tqwc.feastcommon.entity.User;
import com.tqwc.feastweb.mapper.UserMapper;
import com.tqwc.feastweb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
