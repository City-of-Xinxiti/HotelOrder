package org.xfy.hotelpricecomparison.service;

import org.xfy.hotelpricecomparison.dto.request.*;
import org.xfy.hotelpricecomparison.dto.response.*;

/**
 * 供应方API集成服务接口
 * 
 * @author system
 * @since 1.0.0
 */
public interface JinjiangApiService {

    /**
     * 获取品牌列表
     * 
     * @param request 品牌列表查询请求
     * @return 品牌列表响应
     */
    JinjiangApiResponse<JinjiangBrandResponse> getBrandList(JinjiangBrandListRequest request);

    /**
     * 获取酒店ID列表
     * 
     * @param request 酒店ID列表查询请求
     * @return 酒店ID列表响应
     */
    JinjiangApiResponse<JinjiangHotelIdsResponse> getHotelIds(JinjiangHotelIdsRequest request);

    /**
     * 获取酒店基本信息
     * 
     * @param request 酒店信息查询请求
     * @return 酒店信息响应
     */
    JinjiangApiResponse<JinjiangHotelInfoResponse> getHotelInfo(JinjiangHotelInfoRequest request);

    /**
     * 获取酒店房型信息
     * 
     * @param request 房型信息查询请求
     * @return 房型信息响应
     */
    JinjiangApiResponse<JinjiangHotelRoomTypeResponse> getHotelRoomType(JinjiangHotelRoomTypeRequest request);

    /**
     * 获取酒店实时房态
     * 
     * @param request 房态查询请求
     * @return 房态信息响应
     */
    JinjiangApiResponse<JinjiangHotelRoomStatusResponse> getHotelRealRoomStatus(JinjiangHotelRoomStatusRequest request);

    /**
     * 下单预订
     * 
     * @param request 下单请求
     * @return 下单响应
     */
    JinjiangApiResponse<JinjiangPostOrderResponse> postOrder(JinjiangPostOrderRequest request);

    /**
     * 取消订单
     * 
     * @param request 取消订单请求
     * @return 取消订单响应
     */
    JinjiangApiResponse<JinjiangCancelOrderResponse> cancelOrder(JinjiangCancelOrderRequest request);

    /**
     * 获取酒店图片
     * 
     * @param request 酒店图片查询请求
     * @return 酒店图片响应
     */
    JinjiangApiResponse<JinjiangHotelImageResponse> getHotelImages(JinjiangHotelImageRequest request);
}
