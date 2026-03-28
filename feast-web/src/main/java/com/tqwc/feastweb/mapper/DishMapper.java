package com.tqwc.feastweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tqwc.feastcommon.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品表 Mapper 接口
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
