package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.Relationship;
import com.tqwc.feastcommon.entity.User;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feastweb.service.RelationshipService;
import com.tqwc.feastweb.service.UserService;
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

    @Autowired
    private UserService userService;

    /**
     * 查询当前用户的有效情侣关系
     *
     * 请求示例：
     * GET /relationship/current?userId=1
     *
     * @param userId 当前用户ID
     * @return 统一返回结果，data 中为当前有效关系
     */
    @GetMapping("/current")
    public Result getCurrentRelationship(@RequestParam Long userId) {

        // 调用业务层查询当前有效关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(userId);
        if (relationship != null) {
            User user1 = userService.getById(relationship.getUser1Id());
            User user2 = userService.getById(relationship.getUser2Id());
            relationship.setUser1Avatar(user1 == null ? null : user1.getAvatar());
            relationship.setUser2Avatar(user2 == null ? null : user2.getAvatar());
        }

        // 返回成功结果，并携带关系数据
        return new Result(StatusCode.OK, "查询成功", relationship);
    }

    /**
     * 解除当前用户的情侣关系
     *
     * 请求示例：
     * POST /relationship/unbind?currentUserId=1
     *
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果
     */
    @PostMapping("/unbind")
    public Result unbind(@RequestParam Long currentUserId) {

        // 调用业务层解除情侣关系
        relationshipService.unbind(currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "情侣关系已解除");
    }

}
