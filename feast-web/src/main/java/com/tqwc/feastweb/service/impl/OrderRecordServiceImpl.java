package com.tqwc.feastweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tqwc.feastcommon.constant.OrderStatus;
import com.tqwc.feastcommon.entity.Dish;
import com.tqwc.feastcommon.entity.OrderItem;
import com.tqwc.feastcommon.entity.OrderRecord;
import com.tqwc.feastcommon.entity.Relationship;
import com.tqwc.feastweb.dto.order.OrderCreateDTO;
import com.tqwc.feastweb.dto.order.OrderItemAddDTO;
import com.tqwc.feastweb.mapper.OrderRecordMapper;
import com.tqwc.feastweb.service.DishService;
import com.tqwc.feastweb.service.OrderItemService;
import com.tqwc.feastweb.service.OrderRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tqwc.feastweb.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class OrderRecordServiceImpl extends ServiceImpl<OrderRecordMapper, OrderRecord> implements OrderRecordService {

    /**
     * 注入情侣关系业务服务
     */
    @Autowired
    private RelationshipService relationshipService;

    /**
     * 注入菜品业务服务
     */
    @Autowired
    private DishService dishService;

    /**
     * 注入订单明细业务服务
     */
    @Autowired
    private OrderItemService orderItemService;

    /**
     * 创建订单
     *
     * @param orderCreateDTO 创建订单请求参数
     */
    @Override
    @Transactional
    public void createOrder(OrderCreateDTO orderCreateDTO) {

        // 1. 判断创建订单参数是否为空
        if (orderCreateDTO == null) {
            throw new RuntimeException("创建订单参数不能为空");
        }

        // 2. 判断当前用户ID是否为空
        if (orderCreateDTO.getCurrentUserId() == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        // 3. 判断订单日期是否为空
        if (orderCreateDTO.getOrderDate() == null) {
            throw new RuntimeException("订单日期不能为空");
        }

        // 4. 判断订单明细是否为空
        if (orderCreateDTO.getItemList() == null || orderCreateDTO.getItemList().isEmpty()) {
            throw new RuntimeException("订单明细不能为空");
        }

        // 5. 查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(orderCreateDTO.getCurrentUserId());
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系，无法创建订单");
        }

        // 6. 创建订单主表对象
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setRelationshipId(relationship.getId());
        orderRecord.setOrderDate(orderCreateDTO.getOrderDate());
        orderRecord.setStatus(OrderStatus.PENDING_CONFIRM);
        orderRecord.setCreatedBy(orderCreateDTO.getCurrentUserId());
        orderRecord.setRemark(orderCreateDTO.getRemark());
        orderRecord.setImage(orderCreateDTO.getImage());
        orderRecord.setCreatedTime(LocalDateTime.now());
        orderRecord.setUpdatedTime(LocalDateTime.now());

        // 7. 保存订单主表
        this.save(orderRecord);

        // 8. 遍历订单明细并逐条保存
        for (OrderItemAddDTO itemDTO : orderCreateDTO.getItemList()) {

            // 8.1 校验菜品ID
            if (itemDTO.getDishId() == null) {
                throw new RuntimeException("菜品ID不能为空");
            }

            // 8.2 校验菜品数量
            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                throw new RuntimeException("菜品数量必须大于0");
            }

            // 8.3 查询菜品是否存在
            Dish dish = dishService.getById(itemDTO.getDishId());
            if (dish == null) {
                throw new RuntimeException("菜品不存在，dishId=" + itemDTO.getDishId());
            }

            // 8.4 判断菜品是否处于正常状态
            if (dish.getStatus() == null || dish.getStatus() != 1) {
                throw new RuntimeException("菜品已停用，无法下单，dishId=" + itemDTO.getDishId());
            }

            // 8.5 创建订单明细对象
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderRecord.getId());
            orderItem.setDishId(itemDTO.getDishId());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setNote(itemDTO.getNote());

            // 8.6 保存订单明细
            orderItemService.save(orderItem);
        }
    }

    /**
     * 查询当前用户所属关系下的订单列表
     *
     * @param currentUserId 当前登录用户ID
     * @return 订单列表
     */
    @Override
    public List<OrderRecord> getMyOrderList(Long currentUserId) {

        //  判断当前用户ID是否为空
        if (currentUserId == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        //  查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(currentUserId);
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系");
        }

        //  构造查询条件
        LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderRecord::getRelationshipId, relationship.getId())
                .orderByDesc(OrderRecord::getCreatedTime);

        //  返回订单列表
        return this.list(wrapper);
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     * @return 订单详情
     */
    @Override
    public Map<String, Object> getOrderDetail(Long orderId, Long currentUserId) {

        //  判断订单ID是否为空
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }

        //  判断当前用户ID是否为空
        if (currentUserId == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        //  查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(currentUserId);
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系");
        }

        //  查询订单主表
        OrderRecord orderRecord = this.getById(orderId);
        if (orderRecord == null) {
            throw new RuntimeException("订单不存在");
        }

        //  判断该订单是否属于当前用户所在的情侣关系
        if (!orderRecord.getRelationshipId().equals(relationship.getId())) {
            throw new RuntimeException("无权查看该订单");
        }

        //  查询订单明细
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId)
                .orderByAsc(OrderItem::getId);

        List<OrderItem> orderItemList = orderItemService.list(wrapper);

        //  查询菜品信息并组装详情项（包含菜名、图片等）
        List<Map<String, Object>> orderItemDetailList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            Dish dish = dishService.getById(orderItem.getDishId());

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", orderItem.getId());
            itemMap.put("dishId", orderItem.getDishId());
            itemMap.put("dishName", dish != null ? dish.getName() : "菜品已删除");
            itemMap.put("dishImage", dish != null ? dish.getImage() : "");
            itemMap.put("dishFlavor", dish != null ? dish.getFlavor() : "");
            itemMap.put("quantity", orderItem.getQuantity());
            itemMap.put("note", orderItem.getNote());
            orderItemDetailList.add(itemMap);
        }

        //  组装返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderRecord", orderRecord);
        resultMap.put("orderItemList", orderItemList);
        resultMap.put("orderItemDetailList", orderItemDetailList);

        return resultMap;
    }

    /**
     * 确认订单
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     */
    @Override
    @Transactional
    public void confirmOrder(Long orderId, Long currentUserId) {
        // 判断订单ID是否为空
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        // 判断当前用户ID是否为空
        if (currentUserId == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }
        // 查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(currentUserId);
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系");
        }
        // 查询订单是否存在
        OrderRecord orderRecord = this.getById(orderId);
        if (orderRecord == null) {
            throw new RuntimeException("订单不存在");
        }
        // 判断订单是否属于当前用户所在的情侣关系
        if (!orderRecord.getRelationshipId().equals(relationship.getId())) {
            throw new RuntimeException("无权确认该订单");
        }
        if (orderRecord.getCreatedBy() != null && orderRecord.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("下单人不能确认自己的订单，请让对方确认菜品");
        }
        // 只有待确认状态的订单才能确认
        if (orderRecord.getStatus() == null || !orderRecord.getStatus().equals(OrderStatus.PENDING_CONFIRM)) {
            throw new RuntimeException("当前订单不是待确认状态，无法确认");
        }
        // 更新订单状态为已确认
        orderRecord.setStatus(OrderStatus.CONFIRMED);
        // 设置确认时间
        orderRecord.setConfirmedTime(LocalDateTime.now());
        // 设置更新时间
        orderRecord.setUpdatedTime(LocalDateTime.now());
        // 更新数据库
        this.updateById(orderRecord);
    }

    /**
     * 完成订单
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     */
    @Override
    @Transactional
    public void completeOrder(Long orderId, Long currentUserId, String image, String remark) {
        // 1. 判断订单ID是否为空
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }

        // 2. 判断当前用户ID是否为空
        if (currentUserId == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        // 3. 查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(currentUserId);
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系");
        }

        // 4. 查询订单是否存在
        OrderRecord orderRecord = this.getById(orderId);
        if (orderRecord == null) {
            throw new RuntimeException("订单不存在");
        }

        // 5. 判断订单是否属于当前用户所在的情侣关系
        if (!orderRecord.getRelationshipId().equals(relationship.getId())) {
            throw new RuntimeException("无权完成该订单");
        }

        if (orderRecord.getCreatedBy() != null && orderRecord.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("下单人不能完成自己的订单，请让对方上传成品");
        }

        // 6. 只有已确认状态的订单才能完成
        if (orderRecord.getStatus() == null || !orderRecord.getStatus().equals(OrderStatus.CONFIRMED)) {
            throw new RuntimeException("当前订单不是已确认状态，无法完成");
        }

        // 7. 更新订单状态为已完成
        orderRecord.setStatus(OrderStatus.COMPLETED);

        // 8. 保存成品图和完成备注
        if (image != null && !image.isBlank()) {
            orderRecord.setImage(image);
        }
        if (remark != null) {
            orderRecord.setRemark(remark);
        }

        // 9. 设置更新时间
        orderRecord.setUpdatedTime(LocalDateTime.now());

        // 10. 更新数据库
        this.updateById(orderRecord);
    }

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param currentUserId 当前登录用户ID
     */
    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long currentUserId) {
        // 1. 判断订单ID是否为空
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }

        // 2. 判断当前用户ID是否为空
        if (currentUserId == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        // 3. 查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(currentUserId);
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系");
        }

        // 4. 查询订单是否存在
        OrderRecord orderRecord = this.getById(orderId);
        if (orderRecord == null) {
            throw new RuntimeException("订单不存在");
        }

        // 5. 判断订单是否属于当前用户所在的情侣关系
        if (!orderRecord.getRelationshipId().equals(relationship.getId())) {
            throw new RuntimeException("无权取消该订单");
        }

        // 6. 已完成订单不能取消
        if (orderRecord.getStatus() != null && orderRecord.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new RuntimeException("已完成订单不能取消");
        }

        // 7. 已取消订单不能重复取消
        if (orderRecord.getStatus() != null && orderRecord.getStatus().equals(OrderStatus.CANCELED)) {
            throw new RuntimeException("该订单已取消，请勿重复操作");
        }

        // 8. 更新订单状态为已取消
        orderRecord.setStatus(OrderStatus.CANCELED);

        // 9. 设置更新时间
        orderRecord.setUpdatedTime(LocalDateTime.now());

        // 10. 更新数据库
        this.updateById(orderRecord);
    }

}
