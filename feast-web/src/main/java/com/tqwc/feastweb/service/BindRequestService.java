package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.BindRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 绑定申请表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface BindRequestService extends IService<BindRequest> {

    /**
     * 发起绑定申请
     * @param fromUserId 发起人用户ID
     * @param toUserId   接收人用户ID
     * @param message    申请附言
     */
    void sendBindRequest(Long fromUserId, Long toUserId, String message);

    /**
     * 查询当前用户收到的待处理申请列表
     * @param userId 当前用户ID
     * @return 待处理申请列表
     */
    List<BindRequest> getReceivedPendingRequests(Long userId);

    /**
     * 查询当前用户发出的申请列表
     * @param userId 当前用户ID
     * @return 申请列表
     */
    List<BindRequest> getSentRequests(Long userId);

    /**
     * 同意绑定申请
     * @param requestId 申请记录ID
     * @param currentUserId 当前登录用户ID
     */
    void acceptRequest(Long requestId, Long currentUserId);

    /**
     * 拒绝绑定申请
     * @param requestId 申请记录ID
     * @param currentUserId 当前登录用户ID
     */
    void rejectRequest(Long requestId, Long currentUserId);

    /**
     * 取消绑定申请
     * @param requestId 申请记录ID
     * @param currentUserId 当前登录用户ID
     */
    void cancelRequest(Long requestId, Long currentUserId);


}
