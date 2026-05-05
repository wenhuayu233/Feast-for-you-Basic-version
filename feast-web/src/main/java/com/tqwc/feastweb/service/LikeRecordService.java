package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.LikeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 点赞表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface LikeRecordService extends IService<LikeRecord> {

    /**
     * 点赞（同一用户对同一菜品仅一条记录）。
     */
    void addLike(Long dishId, Long userId);

    /**
     * 取消点赞；若未点赞则静默成功。
     */
    void removeLike(Long dishId, Long userId);

    boolean liked(Long dishId, Long userId);
}
