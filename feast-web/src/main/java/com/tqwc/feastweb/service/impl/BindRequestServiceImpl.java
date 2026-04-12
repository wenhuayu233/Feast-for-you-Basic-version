package com.tqwc.feastweb.service.impl;

import com.tqwc.feastcommon.entity.BindRequest;
import com.tqwc.feastweb.mapper.BindRequestMapper;
import com.tqwc.feastweb.service.BindRequestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tqwc.feastweb.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tqwc.feastcommon.constant.BindRequestStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
/**
 * <p>
 * 绑定申请表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class BindRequestServiceImpl extends ServiceImpl<BindRequestMapper, BindRequest> implements BindRequestService {

    @Autowired
    private RelationshipService relationshipService;
    /**
     * 发起绑定申请
     *
     * @param fromUserId 发起人用户ID
     * @param toUserId   接收人用户ID
     * @param message    申请附言
     */
    @Override
    public void sendBindRequest(Long fromUserId, Long toUserId, String message) {
        if (fromUserId.equals(toUserId)) {
            throw new RuntimeException("不能给自己发送绑定申请");
        }

        BindRequest bindRequest = new BindRequest();
        bindRequest.setFromUserId(fromUserId);
        bindRequest.setToUserId(toUserId);
        bindRequest.setMessage(message);
        bindRequest.setStatus(BindRequestStatus.PENDING);
        bindRequest.setCreatedTime(LocalDateTime.now());

        this.save(bindRequest);
    }

    /**
     * 查询当前用户收到的待处理申请列表
     *
     * @param userId 当前用户ID
     * @return 待处理申请列表
     */
    @Override
    public List<BindRequest> getReceivedPendingRequests(Long userId) {
        LambdaQueryWrapper<BindRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BindRequest::getToUserId, userId)
                .eq(BindRequest::getStatus, BindRequestStatus.PENDING)
                .orderByDesc(BindRequest::getCreatedTime);

        return this.list(wrapper);
    }

    /**
     * 查询当前用户发出的申请列表
     *
     * @param userId 当前用户ID
     * @return 申请列表
     */
    @Override
    public List<BindRequest> getSentRequests(Long userId) {
        LambdaQueryWrapper<BindRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BindRequest::getFromUserId, userId)
                .orderByDesc(BindRequest::getCreatedTime);

        return this.list(wrapper);
    }

    /**
     * 同意绑定申请
     *
     * @param requestId      申请记录ID
     * @param currentUserId  当前登录用户ID
     */
    @Override
    @Transactional
    public void acceptRequest(Long requestId, Long currentUserId) {
        // 根据申请ID查询申请记录
        BindRequest bindRequest = this.getById(requestId);

        // 判断申请是否存在
        if (bindRequest == null) {
            throw new RuntimeException("申请记录不存在");
        }

        // 判断当前用户是否有权限处理该申请
        // 只有接收方才能同意申请
        if (!bindRequest.getToUserId().equals(currentUserId)) {
            throw new RuntimeException("无权处理该申请");
        }

        // 判断当前申请是否仍然是待处理状态
        if (!bindRequest.getStatus().equals(BindRequestStatus.PENDING)) {
            throw new RuntimeException("该申请不是待处理状态");
        }

        // 判断发起人当前是否已经存在有效关系
        if (relationshipService.getActiveRelationshipByUserId(bindRequest.getFromUserId()) != null) {
            throw new RuntimeException("发起人当前已存在有效情侣关系");
        }

        // 判断接收人当前是否已经存在有效关系
        if (relationshipService.getActiveRelationshipByUserId(bindRequest.getToUserId()) != null) {
            throw new RuntimeException("接收人当前已存在有效情侣关系");
        }

        // 更新申请状态为“已同意”
        bindRequest.setStatus(BindRequestStatus.ACCEPTED);

        // 设置处理时间
        bindRequest.setHandledTime(LocalDateTime.now());

        // 更新申请记录
        this.updateById(bindRequest);

        // 创建正式情侣关系
        relationshipService.createRelationship(bindRequest.getFromUserId(), bindRequest.getToUserId());

        // 后面这里再补创建 relationship 的逻辑
    }

    /**
     * 拒绝绑定申请
     *
     * @param requestId      申请记录ID
     * @param currentUserId  当前登录用户ID
     */
    @Override
    public void rejectRequest(Long requestId, Long currentUserId) {
        // 根据申请ID查询申请记录
        BindRequest bindRequest = this.getById(requestId);
        // 判断申请是否存在
        if (bindRequest == null) {
            throw new RuntimeException("申请记录不存在");
        }
        // 判断当前用户是否有权限处理该申请
        // 只有接收方才能拒绝申请
        if (!bindRequest.getToUserId().equals(currentUserId)) {
            throw new RuntimeException("无权处理该申请");
        }
        // 判断当前申请是否仍然是待处理状态
        if (!bindRequest.getStatus().equals(BindRequestStatus.PENDING)) {
            throw new RuntimeException("该申请不是待处理状态");
        }
        // 更新申请状态为“已拒绝”
        bindRequest.setStatus(BindRequestStatus.REJECTED);
        // 设置处理时间
        bindRequest.setHandledTime(LocalDateTime.now());
        // 更新数据库
        this.updateById(bindRequest);
    }

    /**
     * 取消绑定申请
     *
     * @param requestId      申请记录ID
     * @param currentUserId  当前登录用户ID
     */
    @Override
    public void cancelRequest(Long requestId, Long currentUserId) {
        BindRequest bindRequest = this.getById(requestId);
        if (bindRequest == null) {
            throw new RuntimeException("申请记录不存在");
        }

        if (!bindRequest.getFromUserId().equals(currentUserId)) {
            throw new RuntimeException("无权取消该申请");
        }

        if (!bindRequest.getStatus().equals(BindRequestStatus.PENDING)) {
            throw new RuntimeException("只有待处理状态才能取消");
        }

        bindRequest.setStatus(BindRequestStatus.CANCELED);
        bindRequest.setHandledTime(LocalDateTime.now());

        this.updateById(bindRequest);
    }


}
