package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

/**
 * 锦江取消订单请求DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangCancelOrderRequest {

    /**
     * 订单号
     */
    private String orderCode;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 备注
     */
    private String remarks;
}
