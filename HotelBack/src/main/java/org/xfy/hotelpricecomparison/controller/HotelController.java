package org.xfy.hotelpricecomparison.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.xfy.hotelpricecomparison.common.ApiResponse;
import org.xfy.hotelpricecomparison.entity.JinjiangBrand;
import org.xfy.hotelpricecomparison.entity.JinjiangHotel;
import org.xfy.hotelpricecomparison.entity.JinjiangRoomType;
import org.xfy.hotelpricecomparison.entity.JinjiangRoomStatus;
import org.xfy.hotelpricecomparison.service.HotelService;

import java.util.List;
import java.util.Map;

/**
 * 酒店数据控制器
 * 直接使用数据库数据，不调用外部API
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    /**
     * 获取品牌列表
     */
    @GetMapping("/brands")
    public ApiResponse<List<JinjiangBrand>> getBrands() {
        try {
            log.info("获取品牌列表");
            List<JinjiangBrand> brands = hotelService.getAllBrands();
            return ApiResponse.success(brands);
        } catch (Exception e) {
            log.error("获取品牌列表失败", e);
            return ApiResponse.error("获取品牌列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取酒店列表
     */
    @GetMapping("/hotels")
    public ApiResponse<List<JinjiangHotel>> getHotels(
            @RequestParam(required = false) String cityCode,
            @RequestParam(required = false) String brandCode,
            @RequestParam(required = false) Integer status) {
        try {
            log.info("获取酒店列表 - cityCode: {}, brandCode: {}, status: {}", cityCode, brandCode, status);
            List<JinjiangHotel> hotels = hotelService.getHotels(cityCode, brandCode, status);
            return ApiResponse.success(hotels);
        } catch (Exception e) {
            log.error("获取酒店列表失败", e);
            return ApiResponse.error("获取酒店列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据酒店ID获取酒店信息
     */
    @GetMapping("/hotels/{innId}")
    public ApiResponse<JinjiangHotel> getHotelById(@PathVariable String innId) {
        try {
            log.info("获取酒店信息 - innId: {}", innId);
            JinjiangHotel hotel = hotelService.getHotelById(innId);
            if (hotel == null) {
                return ApiResponse.error("酒店不存在");
            }
            return ApiResponse.success(hotel);
        } catch (Exception e) {
            log.error("获取酒店信息失败", e);
            return ApiResponse.error("获取酒店信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取酒店房型列表
     */
    @GetMapping("/hotels/{innId}/room-types")
    public ApiResponse<List<JinjiangRoomType>> getHotelRoomTypes(@PathVariable String innId) {
        try {
            log.info("获取酒店房型列表 - innId: {}", innId);
            List<JinjiangRoomType> roomTypes = hotelService.getHotelRoomTypes(innId);
            return ApiResponse.success(roomTypes);
        } catch (Exception e) {
            log.error("获取酒店房型列表失败", e);
            return ApiResponse.error("获取酒店房型列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取酒店实时房态
     */
    @PostMapping("/hotels/{innId}/room-status")
    public ApiResponse<List<JinjiangRoomStatus>> getHotelRoomStatus(
            @PathVariable String innId,
            @RequestBody Map<String, Object> request) {
        try {
            log.info("获取酒店实时房态 - innId: {}, request: {}", innId, request);
            
            // 从请求中提取参数
            Long endOfDay = request.get("endOfDay") != null ? 
                Long.valueOf(request.get("endOfDay").toString()) : null;
            Integer days = request.get("days") != null ? 
                Integer.valueOf(request.get("days").toString()) : 1;
            Integer priceType = request.get("priceType") != null ? 
                Integer.valueOf(request.get("priceType").toString()) : 1;
            
            List<JinjiangRoomStatus> roomStatus = hotelService.getHotelRoomStatus(innId, endOfDay, days, priceType);
            return ApiResponse.success(roomStatus);
        } catch (Exception e) {
            log.error("获取酒店实时房态失败", e);
            return ApiResponse.error("获取酒店实时房态失败: " + e.getMessage());
        }
    }

    /**
     * 创建订单
     */
    @PostMapping("/orders")
    public ApiResponse<Map<String, Object>> createOrder(@RequestBody Map<String, Object> orderData) {
        try {
            log.info("创建订单: {}", orderData);
            Map<String, Object> result = hotelService.createOrder(orderData);
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return ApiResponse.error("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    @PostMapping("/orders/{orderId}/cancel")
    public ApiResponse<Map<String, Object>> cancelOrder(@PathVariable Long orderId) {
        try {
            log.info("取消订单 - orderId: {}", orderId);
            Map<String, Object> result = hotelService.cancelOrder(orderId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("取消订单失败", e);
            return ApiResponse.error("取消订单失败: " + e.getMessage());
        }
    }
}
