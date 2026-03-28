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
 * 订单表
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Getter
@Setter
@ToString
@TableName("order_record")
public class OrderRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 对应情侣关系ID
     */
    @TableField("relationship_id")
    private Long relationshipId;

    /**
     * 订单日期/做饭日期
     */
    @TableField("order_date")
    private LocalDate orderDate;

    /**
     * 状态：0待确认，1已确认，2已完成，3已取消
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 图片地址
     */
    @TableField("image")
    private String image;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 确认时间
     */
    @TableField("confirmed_time")
    private LocalDateTime confirmedTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}
