package com.tqwc.feastweb.dto.dish;

import com.tqwc.feastcommon.entity.Dish;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tang
 * @data 2026/6/12 15:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DishResponse extends DishCreateRequest {
    private Long id;
    private Long createdBy;
    private Integer status;
    private Boolean liked;
    private Boolean favorited;

    public static DishResponse from(Dish dish, boolean liked, boolean favorited) {
        DishResponse response = new DishResponse();
        response.setId(dish.getId());
        response.setName(dish.getName());
        response.setFlavor(dish.getFlavor());
        response.setDescription(dish.getDescription());
        response.setImage(dish.getImage());
        response.setCreatedBy(dish.getCreatedBy());
        response.setStatus(dish.getStatus());
        response.setLiked(liked);
        response.setFavorited(favorited);
        return response;
    }
}
