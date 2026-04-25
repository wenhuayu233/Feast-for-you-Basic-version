package com.tqwc.feastweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tqwc.feastcommon.constant.RelationshipStatus;
import com.tqwc.feastcommon.entity.Relationship;
import com.tqwc.feastweb.mapper.RelationshipMapper;
import com.tqwc.feastweb.service.RelationshipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * 情侣关系表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper, Relationship> implements RelationshipService {

    /**
     * 根据用户ID查询当前有效的情侣关系
     *
     * @param userId 用户ID
     * @return 当前有效关系；如果不存在则返回 null
     */
    @Override
    public Relationship getActiveRelationshipByUserId(Long userId) {

        // 构造查询条件
        LambdaQueryWrapper<Relationship> wrapper = new LambdaQueryWrapper<>();

        // 查询条件：
        // 1. 关系状态必须为有效
        // 2. 当前用户要么是 user1，要么是 user2
        wrapper.eq(Relationship::getStatus, RelationshipStatus.ACTIVE)
                .and(w -> w.eq(Relationship::getUser1Id, userId)
                        .or()
                        .eq(Relationship::getUser2Id, userId))
                .orderByDesc(Relationship::getCreatedTime)
                .orderByDesc(Relationship::getId)
                .last("LIMIT 1");

        // 兼容历史脏数据：可能存在多条有效关系，取最新一条避免 selectOne 异常
        return this.list(wrapper).stream().findFirst().orElse(null);
    }

    /**
     * 创建情侣关系
     *
     * @param user1Id 用户1 ID
     * @param user2Id 用户2 ID
     */
    @Override
    public void createRelationship(Long user1Id, Long user2Id) {

        // 防止把自己和自己建立关系
        if (user1Id.equals(user2Id)) {
            throw new RuntimeException("不能和自己建立情侣关系");
        }

        // 为了避免 A-B 和 B-A 重复，统一按大小排序存储
        Long minUserId = Math.min(user1Id, user2Id);
        Long maxUserId = Math.max(user1Id, user2Id);

        // 创建 Relationship 对象
        Relationship relationship = new Relationship();
        relationship.setUser1Id(minUserId);
        relationship.setUser2Id(maxUserId);

        // 生成关系唯一识别码
        relationship.setRelationCode(UUID.randomUUID().toString().replace("-", ""));

        // 设置关系状态为有效
        relationship.setStatus(RelationshipStatus.ACTIVE);

        // 设置建立时间
        relationship.setStartDate(LocalDateTime.now());
        relationship.setCreatedTime(LocalDateTime.now());
        relationship.setUpdatedTime(LocalDateTime.now());

        // 保存到数据库
        this.save(relationship);
    }

    /**
     * 解除情侣关系
     *
     * @param currentUserId 当前登录用户ID
     */
    @Override
    public void unbind(Long currentUserId) {

        // 先查询当前用户是否存在有效关系
        Relationship relationship = this.getActiveRelationshipByUserId(currentUserId);

        // 如果没有有效关系，直接报错
        if (relationship == null) {
            throw new RuntimeException("当前用户不存在有效的情侣关系");
        }

        // 将关系状态改为已解除
        relationship.setStatus(RelationshipStatus.INACTIVE);

        // 设置解除时间
        relationship.setEndDate(LocalDateTime.now());

        // 更新时间
        relationship.setUpdatedTime(LocalDateTime.now());

        // 更新数据库
        this.updateById(relationship);
    }

}
