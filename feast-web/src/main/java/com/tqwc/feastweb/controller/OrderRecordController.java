package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.OrderRecord;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feastweb.dto.order.OrderCreateDTO;
import com.tqwc.feastweb.service.OrderRecordService;
import com.tqwc.feastweb.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * 说明：
 * 该控制器负责订单主流程相关接口，
 * 当前第一阶段主要提供：
 * 1. 创建订单
 * 2. 查询我的订单列表
 * 3. 查询订单详情
 *
 * @author Tang
 * @data 2026/4/14 20:39
 */

@RestController
@RequestMapping("/order")
public class OrderRecordController {
    /**
     * 注入订单业务服务
     */
    @Autowired
    private OrderRecordService orderRecordService;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 创建订单
     *
     * 请求方式：
     * POST /order/create
     *
     * 请求体示例：
     * {
     *   "currentUserId": 1,
     *   "orderDate": "2026-04-15",
     *   "remark": "晚餐做这些菜",
     *   "image": "xxx.jpg",
     *   "itemList": [
     *     {
     *       "dishId": 1,
     *       "quantity": 2,
     *       "note": "少辣"
     *     },
     *     {
     *       "dishId": 3,
     *       "quantity": 1,
     *       "note": "不要香菜"
     *     }
     *   ]
     * }
     *
     * @param orderCreateDTO 创建订单请求参数
     * @return 统一返回结果
     */
    @PostMapping("/create")
    public Result createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {

        // 调用业务层创建订单
        orderRecordService.createOrder(orderCreateDTO);

        // 返回成功结果
        return new Result(StatusCode.OK, "订单创建成功");
    }

    /**
     * 查询当前用户所属关系下的订单列表
     *
     * 请求方式：
     * GET /order/myList?currentUserId=1
     *
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果，data 中为订单列表
     */
    @GetMapping("/myList")
    public Result getMyOrderList(@RequestParam Long currentUserId) {

        // 调用业务层查询订单列表
        List<OrderRecord> orderRecordList = orderRecordService.getMyOrderList(currentUserId);

        // 返回成功结果，并携带数据
        return new Result(StatusCode.OK, "查询成功", orderRecordList);
    }

    /**
     * 查询订单详情
     *
     * 请求方式：
     * GET /order/detail/{orderId}?currentUserId=1
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果，data 中包含订单主表和订单明细
     */
    @GetMapping("/detail/{orderId}")
    public Result getOrderDetail(@PathVariable Long orderId,
                                 @RequestParam Long currentUserId) {

        // 调用业务层查询订单详情
        Map<String, Object> resultMap = orderRecordService.getOrderDetail(orderId, currentUserId);

        // 返回成功结果，并携带数据
        return new Result(StatusCode.OK, "查询成功", resultMap);
    }

    /**
     * 确认订单
     *
     * 请求方式：
     * POST /order/confirm/{orderId}?currentUserId=1
     *
     * 说明：
     * 当前接口先预留，等 service 层 confirmOrder 实现后即可使用。
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果
     */
    @PostMapping("/confirm/{orderId}")
    public Result confirmOrder(@PathVariable Long orderId,
                               @RequestParam Long currentUserId) {

        // 调用业务层确认订单
        orderRecordService.confirmOrder(orderId, currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "订单确认成功");
    }

    /**
     * 完成订单
     *
     * 请求方式：
     * POST /order/complete/{orderId}?currentUserId=1
     *
     * 说明：
     * 当前接口先预留，等 service 层 completeOrder 实现后即可使用。
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果
     */
    @PostMapping(value = "/complete/{orderId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result completeOrder(@PathVariable Long orderId,
                                @RequestParam Long currentUserId,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                @RequestParam(value = "remark", required = false) String remark) {

        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                imageUrl = minioUtil.uploadFile(file);
            } catch (Exception e) {
                return new Result(StatusCode.ERROR, "成品图片上传失败: " + e.getMessage());
            }
        }

        // 调用业务层完成订单
        orderRecordService.completeOrder(orderId, currentUserId, imageUrl, remark);

        // 返回成功结果
        return new Result(StatusCode.OK, "订单已完成");
    }

    /**
     * 取消订单
     *
     * 请求方式：
     * POST /order/cancel/{orderId}?currentUserId=1
     *
     * 说明：
     * 当前接口先预留，等 service 层 cancelOrder 实现后即可使用。
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果
     */
    @PostMapping("/cancel/{orderId}")
    public Result cancelOrder(@PathVariable Long orderId,
                              @RequestParam Long currentUserId) {

        // 调用业务层取消订单
        orderRecordService.cancelOrder(orderId, currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "订单已取消");
    }

}
