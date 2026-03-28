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
 * 情侣关系表
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Getter
@Setter
@ToString
@TableName("relationship")
public class Relationship implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 情侣关系ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户1
     */
    @TableField("user1_id")
    private Long user1Id;

    /**
     * 用户2
     */
    @TableField("user2_id")
    private Long user2Id;

    /**
     * 关系唯一识别码
     */
    @TableField("relation_code")
    private String relationCode;

    /**
     * 状态：1有效，0已解除
     */
    @TableField("status")
    private Integer status;

    /**
     * 建立时间
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 解除时间
     */
    @TableField("end_date")
    private LocalDateTime endDate;

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
