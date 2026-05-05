package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.Dish;
import com.tqwc.feastweb.dto.dish.DishCreateRequest;
import com.tqwc.feastweb.dto.dish.DishUpdateRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface DishService extends IService<Dish> {

    Dish create(DishCreateRequest request, Long userId);

    Dish update(Long id, DishUpdateRequest request, Long userId);

    void softDelete(Long id, Long userId);

    Dish getActiveById(Long id);

    List<Dish> listActive();

    List<Dish> listActiveByCreatorIds(List<Long> creatorIds);

    void softDeleteByCreatorIds(List<Long> creatorIds);
}
