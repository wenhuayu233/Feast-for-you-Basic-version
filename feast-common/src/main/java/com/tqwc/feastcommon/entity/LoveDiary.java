package com.tqwc.feastcommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 恋爱日记表
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Getter
@Setter
@ToString
@TableName("love_diary")
public class LoveDiary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日记ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 情侣关系ID
     */
    @TableField("relationship_id")
    private Long relationshipId;

    /**
     * 关联订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 日记日期
     */
    @TableField("diary_date")
    private LocalDate diaryDate;

    /**
     * 日记标题
     */
    @TableField("title")
    private String title;

    /**
     * 日记内容
     */
    @TableField("content")
    private String content;

    /**
     * 图片地址
     */
    @TableField("image")
    private String image;

    /**
     * 样式标签，如爱心、餐具
     */
    @TableField("style_tag")
    private String styleTag;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

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
