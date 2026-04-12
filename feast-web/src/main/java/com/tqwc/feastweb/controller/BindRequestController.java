package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.BindRequest;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feastweb.service.BindRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 绑定申请表 前端控制器
 * </p>
 *
 * @author Tang
 * @data 2026/4/12 13:19
 */
@RestController
@RequestMapping("/bind/request")
public class BindRequestController {

    /**
     * 注入绑定申请业务服务
     */
    @Autowired
    private BindRequestService bindRequestService;

    /**
     * 发起绑定申请
     *
     * 说明：
     * 当前先为了方便测试，直接通过请求参数传入发起人ID、接收人ID和申请附言。
     * 后续如果接入登录功能，可以把 fromUserId 改成从当前登录用户信息中获取。
     *
     * 请求示例：
     * POST /bind/request/send?fromUserId=1&toUserId=2&message=你好，和我绑定吧
     *
     * @param fromUserId 发起人用户ID
     * @param toUserId 接收人用户ID
     * @param message 申请附言
     * @return 返回操作结果
     */
    @PostMapping("/send")
    public Result sendBindRequest(@RequestParam Long fromUserId,
                                  @RequestParam Long toUserId,
                                  @RequestParam(required = false) String message) {

        // 调用业务层发起绑定申请
        bindRequestService.sendBindRequest(fromUserId, toUserId, message);

        // 返回成功结果
        return new Result(StatusCode.OK, "绑定申请发送成功");
    }

    /**
     * 查询当前用户收到的待处理申请列表
     *
     * 请求示例：
     * GET /bind/request/received?userId=2
     *
     * @param userId 当前用户ID
     * @return 统一返回结果，data 中为待处理申请列表
     */
    @GetMapping("/received")
    public Result getReceivedPendingRequests(@RequestParam Long userId) {

        // 调用业务层查询收到的待处理申请
        List<BindRequest> list = bindRequestService.getReceivedPendingRequests(userId);

        // 返回成功结果，并携带数据
        return new Result(StatusCode.OK, "查询成功", list);
    }

    /**
     * 查询当前用户发出的申请列表
     *
     * 请求示例：
     * GET /bind/request/sent?userId=1
     *
     * @param userId 当前用户ID
     * @return 统一返回结果，data 中为申请列表
     */
    @GetMapping("/sent")
    public Result getSentRequests(@RequestParam Long userId) {

        // 调用业务层查询发出的申请
        List<BindRequest> list = bindRequestService.getSentRequests(userId);

        // 返回成功结果，并携带数据
        return new Result(StatusCode.OK, "查询成功", list);
    }

    /**
     * 同意绑定申请
     *
     * 请求示例：
     * POST /bind/request/accept/1?currentUserId=2
     *
     * @param requestId 申请记录ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果
     */
    @PostMapping("/accept/{requestId}")
    public Result acceptRequest(@PathVariable Long requestId,
                                @RequestParam Long currentUserId) {

        // 调用业务层同意绑定申请
        bindRequestService.acceptRequest(requestId, currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "绑定申请已同意");
    }

    /**
     * 拒绝绑定申请
     *
     * 请求示例：
     * POST /bind/request/reject/1?currentUserId=2
     *
     * @param requestId 申请记录ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果
     */
    @PostMapping("/reject/{requestId}")
    public Result rejectRequest(@PathVariable Long requestId,
                                @RequestParam Long currentUserId) {

        // 调用业务层拒绝绑定申请
        bindRequestService.rejectRequest(requestId, currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "绑定申请已拒绝");
    }

    /**
     * 取消绑定申请
     *
     * 请求示例：
     * POST /bind/request/cancel/1?currentUserId=1
     *
     * @param requestId 申请记录ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果
     */
    @PostMapping("/cancel/{requestId}")
    public Result cancelRequest(@PathVariable Long requestId,
                                @RequestParam Long currentUserId) {

        // 调用业务层取消绑定申请
        bindRequestService.cancelRequest(requestId, currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "绑定申请已取消");
    }

}
