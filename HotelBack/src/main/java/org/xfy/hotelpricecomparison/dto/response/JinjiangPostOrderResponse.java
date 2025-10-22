package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;

/**
 * 供应方下单预订响应DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangPostOrderResponse {

    /**
     * 订单号
     */
    private String orderCode;

    /**
     * 订单状态
     */
    private Integer orderState;
}
