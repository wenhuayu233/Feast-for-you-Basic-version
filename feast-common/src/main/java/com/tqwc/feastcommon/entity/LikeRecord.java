package com.tqwc.feastcommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 点赞表
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Getter
@Setter
@ToString
@TableName("like_record")
public class LikeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点赞记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 菜品ID
     */
    @TableField("dish_id")
    private Long dishId;

    /**
     * 点赞时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;
}
