package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

/**
 * 供应方酒店房型请求DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangHotelRoomTypeRequest {

    /**
     * 酒店ID
     */
    private String hotelId;

    /**
     * 房型ID
     */
    private String roomTypeId;

    /**
     * 入住日期
     */
    private String checkInDate;

    /**
     * 离店日期
     */
    private String checkOutDate;
}