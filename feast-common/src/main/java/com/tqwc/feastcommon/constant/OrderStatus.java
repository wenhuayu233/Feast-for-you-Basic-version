package com.tqwc.feastcommon.constant;

/**
 * 单状态常量类
 * @author Tang
 * @data 2026/4/14 15:35
 */
public class OrderStatus {

    /**
     * 私有构造方法，防止外部创建对象
     */
    private OrderStatus() {
    }

    /**
     * 待确认
     */
    public static final int PENDING_CONFIRM = 0;

    /**
     * 已确认
     */
    public static final int CONFIRMED = 1;

    /**
     * 已完成
     */
    public static final int COMPLETED = 2;

    /**
     * 已取消
     */
    public static final int CANCELED = 3;


}
