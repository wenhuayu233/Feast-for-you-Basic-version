package com.tqwc.feastweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tqwc.feastcommon.entity.LikeRecord;
import com.tqwc.feastweb.mapper.LikeRecordMapper;
import com.tqwc.feastweb.service.DishService;
import com.tqwc.feastweb.service.LikeRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 点赞表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class LikeRecordServiceImpl extends ServiceImpl<LikeRecordMapper, LikeRecord> implements LikeRecordService {

    private final DishService dishService;

    public LikeRecordServiceImpl(DishService dishService) {
        this.dishService = dishService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLike(Long dishId, Long userId) {
        if (Objects.isNull(dishService.getActiveById(dishId))) {
            throw new IllegalArgumentException("菜品不存在或已下架");
        }
        if (liked(dishId, userId)) {
            throw new IllegalArgumentException("已点赞");
        }
        LikeRecord record = new LikeRecord();
        record.setUserId(userId);
        record.setDishId(dishId);
        record.setCreatedTime(LocalDateTime.now());
        this.save(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLike(Long dishId, Long userId) {
        LambdaQueryWrapper<LikeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeRecord::getDishId, dishId).eq(LikeRecord::getUserId, userId);
        this.remove(wrapper);
    }

    private boolean liked(Long dishId, Long userId) {
        LambdaQueryWrapper<LikeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeRecord::getDishId, dishId).eq(LikeRecord::getUserId, userId);
        return this.count(wrapper) > 0;
    }
}
