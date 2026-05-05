package com.tqwc.feastweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tqwc.feastcommon.entity.Dish;
import com.tqwc.feastweb.dto.dish.DishCreateRequest;
import com.tqwc.feastweb.dto.dish.DishUpdateRequest;
import com.tqwc.feastweb.mapper.DishMapper;
import com.tqwc.feastweb.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 菜品表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    private static final int STATUS_ACTIVE = 1;
    private static final int STATUS_INACTIVE = 0;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Dish create(DishCreateRequest request, Long userId) {
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setFlavor(request.getFlavor());
        dish.setDescription(request.getDescription());
        dish.setImage(request.getImage());
        dish.setCreatedBy(userId);
        dish.setStatus(STATUS_ACTIVE);
        this.save(dish);
        return dish;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Dish update(Long id, DishUpdateRequest request, Long userId) {
        Dish dish = this.getById(id);
        if (Objects.isNull(dish)) {
            throw new IllegalArgumentException("菜品不存在");
        }
        if (!Objects.equals(dish.getStatus(), STATUS_ACTIVE)) {
            throw new IllegalArgumentException("菜品已删除或停用");
        }
        if (!Objects.equals(dish.getCreatedBy(), userId)) {
            throw new IllegalArgumentException("无权修改该菜品");
        }
        dish.setName(request.getName());
        dish.setFlavor(request.getFlavor());
        dish.setDescription(request.getDescription());
        dish.setImage(request.getImage());
        this.updateById(dish);
        return dish;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDelete(Long id, Long userId) {
        Dish dish = this.getById(id);
        if (Objects.isNull(dish)) {
            throw new IllegalArgumentException("菜品不存在");
        }
        if (!Objects.equals(dish.getCreatedBy(), userId)) {
            throw new IllegalArgumentException("无权删除该菜品");
        }
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dish::getId, id).set(Dish::getStatus, STATUS_INACTIVE);
        this.update(wrapper);
    }

    @Override
    public Dish getActiveById(Long id) {
        Dish dish = this.getById(id);
        if (Objects.isNull(dish) || !Objects.equals(dish.getStatus(), STATUS_ACTIVE)) {
            return null;
        }
        return dish;
    }

    @Override
    public List<Dish> listActive() {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, STATUS_ACTIVE).orderByDesc(Dish::getUpdatedTime);
        return this.list(wrapper);
    }

    @Override
    public List<Dish> listActiveByCreatorIds(List<Long> creatorIds) {
        if (Objects.isNull(creatorIds) || creatorIds.isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, STATUS_ACTIVE)
                .in(Dish::getCreatedBy, creatorIds)
                .orderByDesc(Dish::getUpdatedTime);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteByCreatorIds(List<Long> creatorIds) {
        if (Objects.isNull(creatorIds) || creatorIds.isEmpty()) {
            return;
        }
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Dish::getCreatedBy, creatorIds)
                .eq(Dish::getStatus, STATUS_ACTIVE)
                .set(Dish::getStatus, STATUS_INACTIVE);
        this.update(wrapper);
    }
}
