package org.xfy.hotelpricecomparison.common;

import lombok.Data;

/**
 * API响应统一格式
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class ApiResponse<T> {
    
    private Integer code;
    private String message;
    private T result;
    private Long timestamp;
    
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public ApiResponse(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(0, "success", result);
    }
    
    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(0, "success", null);
    }
    
    /**
     * 错误响应
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(-1, message, null);
    }
    
    /**
     * 错误响应（带错误码）
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
