package org.xfy.hotelpricecomparison.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.xfy.hotelpricecomparison.common.ApiResponse;
import org.xfy.hotelpricecomparison.dto.request.*;
import org.xfy.hotelpricecomparison.dto.response.*;
import org.xfy.hotelpricecomparison.service.JinjiangApiService;

/**
 * 锦江API控制器
 * 提供锦江API的代理接口
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/jinjiang")
@RequiredArgsConstructor
public class JinjiangApiController {

    private final JinjiangApiService jinjiangApiService;

    /**
     * 品牌列表信息查询
     */
    @PostMapping("/brand/getBrandList")
    public ApiResponse<JinjiangBrandResponse> getBrandList(@RequestBody JinjiangBrandListRequest request) {
        try {
            log.info("调用锦江API获取品牌列表: {}", request);
            JinjiangApiResponse<JinjiangBrandResponse> response = jinjiangApiService.getBrandList(request);
            return ApiResponse.success(response.getResult());
        } catch (Exception e) {
            log.error("调用锦江API获取品牌列表失败", e);
            return ApiResponse.error("获取品牌列表失败: " + e.getMessage());
        }
    }

    /**
     * 下单预订
     */
    @PostMapping("/booking/postOrder")
    public ApiResponse<JinjiangPostOrderResponse> postOrder(@RequestBody JinjiangPostOrderRequest request) {
        try {
            log.info("调用锦江API下单预订: {}", request);
            JinjiangApiResponse<JinjiangPostOrderResponse> response = jinjiangApiService.postOrder(request);
            return ApiResponse.success(response.getResult());
        } catch (Exception e) {
            log.error("调用锦江API下单预订失败", e);
            return ApiResponse.error("下单预订失败: " + e.getMessage());
        }
    }

    /**
     * 酒店列表
     */
    @PostMapping("/hotel/getHotelIds")
    public ApiResponse<JinjiangHotelIdsResponse> getHotelIds(@RequestBody JinjiangHotelIdsRequest request) {
        try {
            log.info("调用锦江API获取酒店列表: {}", request);
            JinjiangApiResponse<JinjiangHotelIdsResponse> response = jinjiangApiService.getHotelIds(request);
            return ApiResponse.success(response.getResult());
        } catch (Exception e) {
            log.error("调用锦江API获取酒店列表失败", e);
            return ApiResponse.error("获取酒店列表失败: " + e.getMessage());
        }
    }

    /**
     * 酒店基本信息
     */
    @PostMapping("/hotel/getHotelInfo")
    public ApiResponse<JinjiangHotelInfoResponse> getHotelInfo(@RequestBody JinjiangHotelInfoRequest request) {
        try {
            log.info("调用锦江API获取酒店信息: {}", request);
            JinjiangApiResponse<JinjiangHotelInfoResponse> response = jinjiangApiService.getHotelInfo(request);
            return ApiResponse.success(response.getResult());
        } catch (Exception e) {
            log.error("调用锦江API获取酒店信息失败", e);
            return ApiResponse.error("获取酒店信息失败: " + e.getMessage());
        }
    }

    /**
     * 酒店实时房态
     */
    @PostMapping("/hotel/getHotelRealRoomStatus")
    public ApiResponse<JinjiangHotelRoomStatusResponse> getHotelRealRoomStatus(@RequestBody JinjiangHotelRoomStatusRequest request) {
        try {
            log.info("调用锦江API获取酒店实时房态: {}", request);
            JinjiangApiResponse<JinjiangHotelRoomStatusResponse> response = jinjiangApiService.getHotelRealRoomStatus(request);
            return ApiResponse.success(response.getResult());
        } catch (Exception e) {
            log.error("调用锦江API获取酒店实时房态失败", e);
            return ApiResponse.error("获取酒店实时房态失败: " + e.getMessage());
        }
    }

    /**
     * 酒店房型
     */
    @PostMapping("/hotel/getHotelRoomType")
    public ApiResponse<JinjiangHotelRoomTypeResponse> getHotelRoomType(@RequestBody JinjiangHotelRoomTypeRequest request) {
        try {
            log.info("调用锦江API获取酒店房型: {}", request);
            JinjiangApiResponse<JinjiangHotelRoomTypeResponse> response = jinjiangApiService.getHotelRoomType(request);
            return ApiResponse.success(response.getResult());
        } catch (Exception e) {
            log.error("调用锦江API获取酒店房型失败", e);
            return ApiResponse.error("获取酒店房型失败: " + e.getMessage());
        }
    }
}
