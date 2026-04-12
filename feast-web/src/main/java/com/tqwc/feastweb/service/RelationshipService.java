package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.Relationship;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 情侣关系表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface RelationshipService extends IService<Relationship> {

    /**
     * 根据用户ID查询当前有效的情侣关系
     *
     * 说明：
     * 只要这个用户出现在 user1_id 或 user2_id 中，
     * 并且关系状态为有效，就说明该用户当前已绑定。
     *
     * @param userId 用户ID
     * @return 当前有效关系；如果不存在则返回 null
     */
    Relationship getActiveRelationshipByUserId(Long userId);

    /**
     * 创建情侣关系
     *
     * 说明：
     * 当绑定申请被同意后，正式往 relationship 表插入一条记录。
     *
     * @param user1Id 用户1 ID
     * @param user2Id 用户2 ID
     */
    void createRelationship(Long user1Id, Long user2Id);

    /**
     * 解除情侣关系
     *
     * 说明：
     * 将 relationship 状态改为已解除，并记录解除时间。
     *
     * @param currentUserId 当前登录用户ID
     */
    void unbind(Long currentUserId);

}
