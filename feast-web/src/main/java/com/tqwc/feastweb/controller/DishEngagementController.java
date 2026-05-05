package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.Dish;
import com.tqwc.feastcommon.entity.Relationship;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feast.jwt.JwtUserPrincipal;
import com.tqwc.feastweb.service.DishService;
import com.tqwc.feastweb.service.FavoriteService;
import com.tqwc.feastweb.service.LikeRecordService;
import com.tqwc.feastweb.service.RelationshipService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 菜品点赞、收藏（基于 {@code like_record}、{@code favorite} 表）。
 */
@RestController
@RequestMapping("/dishes")
public class DishEngagementController {

    private final LikeRecordService likeRecordService;
    private final FavoriteService favoriteService;
    private final DishService dishService;
    private final RelationshipService relationshipService;

    public DishEngagementController(LikeRecordService likeRecordService,
                                    FavoriteService favoriteService,
                                    DishService dishService,
                                    RelationshipService relationshipService) {
        this.likeRecordService = likeRecordService;
        this.favoriteService = favoriteService;
        this.dishService = dishService;
        this.relationshipService = relationshipService;
    }

    @PostMapping("/{dishId}/likes")
    public Result like(@PathVariable("dishId") Long dishId, Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        ensureDishVisible(dishId, principal.getUserId());
        likeRecordService.addLike(dishId, principal.getUserId());
        return new Result(StatusCode.OK, "点赞成功");
    }

    @DeleteMapping("/{dishId}/likes")
    public Result unlike(@PathVariable("dishId") Long dishId, Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        ensureDishVisible(dishId, principal.getUserId());
        likeRecordService.removeLike(dishId, principal.getUserId());
        return new Result(StatusCode.OK, "已取消点赞");
    }

    @PostMapping("/{dishId}/favorites")
    public Result favorite(@PathVariable("dishId") Long dishId, Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        ensureDishVisible(dishId, principal.getUserId());
        favoriteService.addFavorite(dishId, principal.getUserId());
        return new Result(StatusCode.OK, "收藏成功");
    }

    @DeleteMapping("/{dishId}/favorites")
    public Result unfavorite(@PathVariable("dishId") Long dishId, Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        ensureDishVisible(dishId, principal.getUserId());
        favoriteService.removeFavorite(dishId, principal.getUserId());
        return new Result(StatusCode.OK, "已取消收藏");
    }

    private void ensureDishVisible(Long dishId, Long currentUserId) {
        Dish dish = dishService.getActiveById(dishId);
        if (Objects.isNull(dish) || !visibleCreatorIds(currentUserId).contains(dish.getCreatedBy())) {
            throw new IllegalArgumentException("菜品不存在或已下架");
        }
    }

    private List<Long> visibleCreatorIds(Long currentUserId) {
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(currentUserId);
        if (Objects.isNull(relationship)) {
            return List.of(currentUserId);
        }
        return List.of(relationship.getUser1Id(), relationship.getUser2Id());
    }

    private static JwtUserPrincipal currentPrincipal(Authentication authentication) {
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof JwtUserPrincipal principal)) {
            return null;
        }
        return principal;
    }
}
