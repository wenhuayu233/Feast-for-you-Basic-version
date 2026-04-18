package com.tqwc.feastweb.dto.order;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Tang
 * @data 2026/4/14 15:38
 */
public class OrderItemAddDTO implements Serializable {


    /**
     * 菜品ID
     */
    private Long dishId;

    /**
     * 菜品数量
     */
    private Integer quantity;

    /**
     * 补充说明
     */
    private String note;

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
