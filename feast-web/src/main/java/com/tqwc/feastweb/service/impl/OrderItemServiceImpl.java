package com.tqwc.feastweb.service.impl;

import com.tqwc.feastcommon.entity.OrderItem;
import com.tqwc.feastweb.mapper.OrderItemMapper;
import com.tqwc.feastweb.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
