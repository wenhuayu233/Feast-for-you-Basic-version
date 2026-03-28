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
 * 绑定申请表
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Getter
@Setter
@ToString
@TableName("bind_request")
public class BindRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申请记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发起申请用户ID
     */
    @TableField("from_user_id")
    private Long fromUserId;

    /**
     * 接收申请用户ID
     */
    @TableField("to_user_id")
    private Long toUserId;

    /**
     * 申请附言
     */
    @TableField("message")
    private String message;

    /**
     * 申请状态：0待处理，1同意，2拒绝，3取消
     */
    @TableField("status")
    private Integer status;

    /**
     * 申请时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 处理时间
     */
    @TableField("handled_time")
    private LocalDateTime handledTime;
}
