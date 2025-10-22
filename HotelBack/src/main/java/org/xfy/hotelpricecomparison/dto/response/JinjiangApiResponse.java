package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;

/**
 * 供应方API通用响应DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangApiResponse<T> {

    /**
     * 错误信息
     */
    private Object errors;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 消息代码
     */
    private Integer msgCode;

    /**
     * 响应结果
     */
    private T result;
}
