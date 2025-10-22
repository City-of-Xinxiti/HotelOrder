package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

/**
 * 供应方酒店信息请求DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangHotelInfoRequest {

    /**
     * 酒店ID
     */
    private String innId;

    /**
     * 语言代码
     */
    private Integer languageCode;
}