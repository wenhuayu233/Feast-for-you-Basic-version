package com.tqwc.feastcommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Getter
@Setter
@ToString
@TableName("order_item")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 菜品ID
     */
    @TableField("dish_id")
    private Long dishId;

    /**
     * 菜品数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 补充说明
     */
    @TableField("note")
    private String note;
}
