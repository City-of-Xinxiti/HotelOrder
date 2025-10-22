package org.xfy.hotelpricecomparison.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xfy.hotelpricecomparison.entity.JinjiangBrand;
import org.xfy.hotelpricecomparison.entity.JinjiangHotel;
import org.xfy.hotelpricecomparison.entity.JinjiangRoomType;
import org.xfy.hotelpricecomparison.entity.JinjiangRoomStatus;
import org.xfy.hotelpricecomparison.mapper.JinjiangBrandMapper;
import org.xfy.hotelpricecomparison.mapper.JinjiangHotelMapper;
import org.xfy.hotelpricecomparison.mapper.JinjiangRoomTypeMapper;
import org.xfy.hotelpricecomparison.mapper.JinjiangRoomStatusMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 酒店数据服务
 * 直接使用数据库数据
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {

    private final JinjiangBrandMapper brandMapper;
    private final JinjiangHotelMapper hotelMapper;
    private final JinjiangRoomTypeMapper roomTypeMapper;
    private final JinjiangRoomStatusMapper roomStatusMapper;

    /**
     * 获取所有品牌
     */
    public List<JinjiangBrand> getAllBrands() {
        // 使用自定义查询方法
        return brandMapper.selectAllBrands();
    }

    /**
     * 获取酒店列表
     */
    public List<JinjiangHotel> getHotels(String cityCode, String brandCode, Integer status) {
        // 这里可以根据条件进行筛选
        // 暂时返回所有有效酒店
        return hotelMapper.selectAllHotels();
    }

    /**
     * 根据酒店ID获取酒店信息
     */
    public JinjiangHotel getHotelById(String innId) {
        return hotelMapper.selectByInnId(innId);
    }

    /**
     * 获取酒店房型列表
     */
    public List<JinjiangRoomType> getHotelRoomTypes(String innId) {
        return roomTypeMapper.selectByInnId(innId);
    }

    /**
     * 获取酒店实时房态
     */
    public List<JinjiangRoomStatus> getHotelRoomStatus(String innId, Long endOfDay, Integer days, Integer priceType) {
        // 如果endOfDay为空，使用当前时间
        if (endOfDay == null) {
            endOfDay = System.currentTimeMillis();
        }
        
        return roomStatusMapper.selectByInnIdAndDateRange(innId, endOfDay, days);
    }

    /**
     * 创建订单
     */
    public Map<String, Object> createOrder(Map<String, Object> orderData) {
        // 这里实现订单创建逻辑
        // 暂时返回成功响应
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", System.currentTimeMillis());
        result.put("orderCode", "JJ" + System.currentTimeMillis());
        result.put("status", "success");
        result.put("message", "订单创建成功");
        
        log.info("订单创建成功: {}", result);
        return result;
    }

    /**
     * 取消订单
     */
    public Map<String, Object> cancelOrder(Long orderId) {
        // 这里实现订单取消逻辑
        // 暂时返回成功响应
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("status", "cancelled");
        result.put("message", "订单取消成功");
        
        log.info("订单取消成功: {}", result);
        return result;
    }
}
