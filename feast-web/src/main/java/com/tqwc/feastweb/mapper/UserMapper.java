package com.tqwc.feastweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tqwc.feastcommon.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
