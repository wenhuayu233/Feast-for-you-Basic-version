package com.tqwc.feastcommon.constant;

/**
 * @author Tang
 * @data 2026/4/11 17:30
 */
public class BindRequestStatus {
    /**
     * 0 = 待处理
     * 1 = 已同意
     * 2 = 已拒绝
     * 3 = 已取消
     */
    public static final int PENDING = 0;
    public static final int ACCEPTED = 1;
    public static final int REJECTED = 2;
    public static final int CANCELED = 3;
}
