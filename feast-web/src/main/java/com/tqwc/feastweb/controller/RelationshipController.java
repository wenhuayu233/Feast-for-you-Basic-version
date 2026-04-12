package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.Relationship;
import com.tqwc.feastweb.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 情侣关系表 前端控制器
 * </p>
 *
 * @author Tang
 * @data 2026/4/12 13:22
 */

@RestController
@RequestMapping("/relationship")
public class RelationshipController {

    /**
     * 注入情侣关系业务服务
     */
    @Autowired
    private RelationshipService relationshipService;

    /**
     * 查询当前用户的有效情侣关系
     *
     * 请求示例：
     * GET /relationship/current?userId=1
     *
     * @param userId 当前用户ID
     * @return 当前有效情侣关系
     */
    @GetMapping("/current")
    public Relationship getCurrentRelationship(@RequestParam Long userId) {

        // 调用业务层查询当前有效关系
        return relationshipService.getActiveRelationshipByUserId(userId);
    }

    /**
     * 解除当前用户的情侣关系
     *
     * 请求示例：
     * POST /relationship/unbind?currentUserId=1
     *
     * @param currentUserId 当前登录用户ID
     * @return 返回操作结果
     */
    @PostMapping("/unbind")
    public String unbind(@RequestParam Long currentUserId) {

        // 调用业务层解除情侣关系
        relationshipService.unbind(currentUserId);

        return "情侣关系已解除";
    }

}
