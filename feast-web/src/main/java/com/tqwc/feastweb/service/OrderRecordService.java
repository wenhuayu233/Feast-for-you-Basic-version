package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.OrderRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tqwc.feastweb.dto.order.OrderCreateDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface OrderRecordService extends IService<OrderRecord> {

    /**
     * 创建订单
     *
     * @param orderCreateDTO 创建订单请求参数
     */
    void createOrder(OrderCreateDTO orderCreateDTO);

    /**
     * 查询当前用户所属关系下的订单列表
     *
     * @param currentUserId 当前登录用户ID
     * @return 订单列表
     */
    List<OrderRecord> getMyOrderList(Long currentUserId);

    /**
     * 查询订单详情
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     * @return 订单详情
     */
    Map<String, Object> getOrderDetail(Long orderId, Long currentUserId);

    /**
     * 确认订单
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     */
    void confirmOrder(Long orderId, Long currentUserId);

    /**
     * 完成订单
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     */
    void completeOrder(Long orderId, Long currentUserId, String image, String remark);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     */
    void cancelOrder(Long orderId, Long currentUserId);


}
