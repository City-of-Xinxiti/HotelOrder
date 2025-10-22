package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;

/**
 * 锦江取消订单响应DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangCancelOrderResponse {

    /**
     * 订单号
     */
    private String orderCode;

    /**
     * 订单状态
     */
    private Integer orderState;

    /**
     * 取消时间
     */
    private String cancelTime;

    /**
     * 退款金额
     */
    private String refundAmount;

    /**
     * 退款状态
     */
    private Integer refundStatus;
}
