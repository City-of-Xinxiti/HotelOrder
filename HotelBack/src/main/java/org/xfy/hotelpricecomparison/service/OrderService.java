package org.xfy.hotelpricecomparison.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.xfy.hotelpricecomparison.entity.Order;


/**
 * 订单服务接口
 * 
 * @author system
 * @since 1.0.0
 */
public interface OrderService {

    /**
     * 获取订单列表
     * 
     * @param customerId 客户ID
     * @param status 订单状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 订单列表
     */
    Page<Order> getOrderList(String customerId, String status, Integer pageNum, Integer pageSize);

    /**
     * 根据ID获取订单详情
     * 
     * @param id 订单ID
     * @return 订单详情
     */
    Order getOrderById(Long id);

    /**
     * 根据订单号获取订单详情
     * 
     * @param orderNumber 订单号
     * @return 订单详情
     */
    Order getOrderByNumber(String orderNumber);

    /**
     * 创建订单
     * 
     * @param order 订单信息
     * @return 创建结果
     */
    boolean createOrder(Order order);

    /**
     * 更新订单状态
     * 
     * @param id 订单ID
     * @param status 新状态
     * @return 更新结果
     */
    boolean updateOrderStatus(Long id, String status);

    /**
     * 取消订单
     * 
     * @param id 订单ID
     * @param reason 取消原因
     * @return 取消结果
     */
    boolean cancelOrder(Long id, String reason);

    /**
     * 删除订单
     * 
     * @param id 订单ID
     * @return 删除结果
     */
    boolean deleteOrder(Long id);
}
