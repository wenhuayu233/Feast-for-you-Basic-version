package com.tqwc.feastweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tqwc.feastcommon.entity.Favorite;
import com.tqwc.feastweb.mapper.FavoriteMapper;
import com.tqwc.feastweb.service.DishService;
import com.tqwc.feastweb.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    private final DishService dishService;

    public FavoriteServiceImpl(DishService dishService) {
        this.dishService = dishService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long dishId, Long userId) {
        if (Objects.isNull(dishService.getActiveById(dishId))) {
            throw new IllegalArgumentException("菜品不存在或已下架");
        }
        if (favorited(dishId, userId)) {
            throw new IllegalArgumentException("已收藏");
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setDishId(dishId);
        favorite.setCreatedTime(LocalDateTime.now());
        this.save(favorite);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long dishId, Long userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getDishId, dishId).eq(Favorite::getUserId, userId);
        this.remove(wrapper);
    }

    private boolean favorited(Long dishId, Long userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getDishId, dishId).eq(Favorite::getUserId, userId);
        return this.count(wrapper) > 0;
    }
}
