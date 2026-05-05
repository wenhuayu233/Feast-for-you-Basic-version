package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface FavoriteService extends IService<Favorite> {

    /**
     * 收藏菜品（同一用户对同一菜品仅一条记录）。
     */
    void addFavorite(Long dishId, Long userId);

    /**
     * 取消收藏；若未收藏则静默成功。
     */
    void removeFavorite(Long dishId, Long userId);

    boolean favorited(Long dishId, Long userId);
}
