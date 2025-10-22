package org.xfy.hotelpricecomparison.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xfy.hotelpricecomparison.entity.Order;
import org.xfy.hotelpricecomparison.mapper.OrderMapper;
import org.xfy.hotelpricecomparison.service.OrderService;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 订单服务实现类
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Override
    public Page<Order> getOrderList(String customerId, String status, Integer pageNum, Integer pageSize) {
        try {
            Page<Order> page = new Page<>(pageNum, pageSize);
            
            LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
            
            if (customerId != null && !customerId.isEmpty()) {
                queryWrapper.eq(Order::getCustomerId, customerId);
            }
            
            if (status != null && !status.isEmpty()) {
                queryWrapper.eq(Order::getStatus, status);
            }
            
            queryWrapper.orderByDesc(Order::getCreateTime);
            
            return orderMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取订单列表失败", e);
            throw new RuntimeException("获取订单列表失败", e);
        }
    }

    @Override
    public Order getOrderById(Long id) {
        try {
            return orderMapper.selectById(id);
        } catch (Exception e) {
            log.error("获取订单详情失败，订单ID: {}", id, e);
            throw new RuntimeException("获取订单详情失败", e);
        }
    }

    @Override
    public Order getOrderByNumber(String orderNumber) {
        try {
            LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Order::getOrderNumber, orderNumber);
            return orderMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            log.error("获取订单详情失败，订单号: {}", orderNumber, e);
            throw new RuntimeException("获取订单详情失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(Order order) {
        try {
            // 生成订单号
            if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
                order.setOrderNumber(generateOrderNumber());
            }
            
            // 设置默认状态
            if (order.getStatus() == null || order.getStatus().isEmpty()) {
                order.setStatus("待确认");
            }
            
            // 设置创建时间
            order.setCreateTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            
            int result = orderMapper.insert(order);
            log.info("创建订单成功，订单号: {}", order.getOrderNumber());
            return result > 0;
        } catch (Exception e) {
            log.error("创建订单失败", e);
            throw new RuntimeException("创建订单失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long id, String status) {
        try {
            Order order = new Order();
            order.setId(id);
            order.setStatus(status);
            order.setUpdateTime(LocalDateTime.now());
            
            int result = orderMapper.updateById(order);
            log.info("更新订单状态成功，订单ID: {}, 新状态: {}", id, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新订单状态失败，订单ID: {}", id, e);
            throw new RuntimeException("更新订单状态失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long id, String reason) {
        try {
            Order order = new Order();
            order.setId(id);
            order.setStatus("已取消");
            order.setUpdateTime(LocalDateTime.now());
            
            int result = orderMapper.updateById(order);
            log.info("取消订单成功，订单ID: {}, 取消原因: {}", id, reason);
            return result > 0;
        } catch (Exception e) {
            log.error("取消订单失败，订单ID: {}", id, e);
            throw new RuntimeException("取消订单失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrder(Long id) {
        try {
            int result = orderMapper.deleteById(id);
            log.info("删除订单成功，订单ID: {}", id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除订单失败，订单ID: {}", id, e);
            throw new RuntimeException("删除订单失败", e);
        }
    }

    /**
     * 生成订单号
     * 
     * @return 订单号
     */
    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
