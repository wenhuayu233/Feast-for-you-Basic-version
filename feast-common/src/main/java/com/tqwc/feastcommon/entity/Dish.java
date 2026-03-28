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
 * 菜品表
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Getter
@Setter
@ToString
@TableName("dish")
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜品名称
     */
    @TableField("name")
    private String name;

    /**
     * 菜品口味
     */
    @TableField("flavor")
    private String flavor;

    /**
     * 菜品描述
     */
    @TableField("description")
    private String description;

    /**
     * 菜品图片地址
     */
    @TableField("image")
    private String image;

    /**
     * 创建者ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 状态：1正常，0删除/停用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}
