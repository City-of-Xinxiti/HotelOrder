package org.xfy.hotelpricecomparison.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.xfy.hotelpricecomparison.common.ApiResponse;
import org.xfy.hotelpricecomparison.entity.Order;
import org.xfy.hotelpricecomparison.service.OrderService;

/**
 * 订单控制器
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 获取订单列表
     */
    @GetMapping
    public ApiResponse<Page<Order>> getOrderList(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            log.info("获取订单列表 - customerId: {}, status: {}, pageNum: {}, pageSize: {}", 
                customerId, status, pageNum, pageSize);
            
            Page<Order> orders = orderService.getOrderList(customerId, status, pageNum, pageSize);
            return ApiResponse.success(orders);
        } catch (Exception e) {
            log.error("获取订单列表失败", e);
            return ApiResponse.error("获取订单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderDetail(@PathVariable Long id) {
        try {
            log.info("获取订单详情 - 订单ID: {}", id);
            Order order = orderService.getOrderById(id);
            if (order == null) {
                return ApiResponse.error("订单不存在");
            }
            return ApiResponse.success(order);
        } catch (Exception e) {
            log.error("获取订单详情失败，订单ID: {}", id, e);
            return ApiResponse.error("获取订单详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据订单号获取订单详情
     */
    @GetMapping("/number/{orderNumber}")
    public ApiResponse<Order> getOrderByNumber(@PathVariable String orderNumber) {
        try {
            log.info("根据订单号获取订单详情 - 订单号: {}", orderNumber);
            Order order = orderService.getOrderByNumber(orderNumber);
            if (order == null) {
                return ApiResponse.error("订单不存在");
            }
            return ApiResponse.success(order);
        } catch (Exception e) {
            log.error("根据订单号获取订单详情失败，订单号: {}", orderNumber, e);
            return ApiResponse.error("获取订单详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建订单
     */
    @PostMapping
    public ApiResponse<Order> createOrder(@RequestBody Order order) {
        try {
            log.info("创建订单 - 酒店: {}, 房型: {}, 客户: {}", 
                order.getHotelName(), order.getRoomTypeName(), order.getCustomerName());
            
            boolean success = orderService.createOrder(order);
            if (success) {
                return ApiResponse.success(order);
            } else {
                return ApiResponse.error("创建订单失败");
            }
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return ApiResponse.error("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<String> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        try {
            log.info("更新订单状态 - 订单ID: {}, 新状态: {}", id, status);
            
            boolean success = orderService.updateOrderStatus(id, status);
            if (success) {
                return ApiResponse.success("更新订单状态成功");
            } else {
                return ApiResponse.error("更新订单状态失败");
            }
        } catch (Exception e) {
            log.error("更新订单状态失败 - 订单ID: {}", id, e);
            return ApiResponse.error("更新订单状态失败: " + e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public ApiResponse<String> cancelOrder(
            @PathVariable Long id, 
            @RequestParam(required = false) String reason) {
        try {
            log.info("取消订单 - 订单ID: {}, 取消原因: {}", id, reason);
            
            boolean success = orderService.cancelOrder(id, reason);
            if (success) {
                return ApiResponse.success("取消订单成功");
            } else {
                return ApiResponse.error("取消订单失败");
            }
        } catch (Exception e) {
            log.error("取消订单失败 - 订单ID: {}", id, e);
            return ApiResponse.error("取消订单失败: " + e.getMessage());
        }
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteOrder(@PathVariable Long id) {
        try {
            log.info("删除订单 - 订单ID: {}", id);
            
            boolean success = orderService.deleteOrder(id);
            if (success) {
                return ApiResponse.success("删除订单成功");
            } else {
                return ApiResponse.error("删除订单失败");
            }
        } catch (Exception e) {
            log.error("删除订单失败 - 订单ID: {}", id, e);
            return ApiResponse.error("删除订单失败: " + e.getMessage());
        }
    }
}
