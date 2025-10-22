package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

/**
 * 锦江酒店图片请求参数
 */
@Data
public class JinjiangHotelImageRequest {
    
    /**
     * 酒店唯一编号
     */
    private String innId;
    
    /**
     * 语言类型，默认值为0
     * 0: 中文
     * 1: 英文
     */
    private String langType = "0";
}
