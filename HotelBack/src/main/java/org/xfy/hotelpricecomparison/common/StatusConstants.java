package org.xfy.hotelpricecomparison.common;

/**
 * 状态常量类
 * 用于替换硬编码的状态值，提高代码可读性和维护性
 * 
 * @author system
 * @since 1.0.0
 */
public class StatusConstants {
    
    // ===== 通用状态 =====
    /**
     * 启用状态
     */
    public static final Integer ENABLED = 1;
    
    /**
     * 禁用状态
     */
    public static final Integer DISABLED = 0;
    
    /**
     * 激活状态
     */
    public static final Integer ACTIVE = 1;
    
    /**
     * 非激活状态
     */
    public static final Integer INACTIVE = 0;
    
    /**
     * 有效状态
     */
    public static final Integer VALID = 1;
    
    /**
     * 无效状态
     */
    public static final Integer INVALID = 0;
    
    // ===== 布尔值状态 =====
    /**
     * 是/真
     */
    public static final Integer TRUE = 1;
    
    /**
     * 否/假
     */
    public static final Integer FALSE = 0;
    
    // ===== 订单状态 =====
    /**
     * 已接受
     */
    public static final Integer ACCEPTED = 1;
    
    /**
     * 未接受
     */
    public static final Integer NOT_ACCEPTED = 0;
    
    /**
     * 已发单
     */
    public static final Integer DISPATCHED = 1;
    
    /**
     * 未发单
     */
    public static final Integer NOT_DISPATCHED = 0;
    
    /**
     * 已锁定
     */
    public static final Integer LOCKED = 1;
    
    /**
     * 未锁定
     */
    public static final Integer NOT_LOCKED = 0;
    
    // ===== 同步状态 =====
    /**
     * 未同步
     */
    public static final Integer NOT_SYNCED = 0;
    
    /**
     * 已同步
     */
    public static final Integer SYNCED = 1;
    
    /**
     * 同步失败
     */
    public static final Integer SYNC_FAILED = 2;
    
    // ===== 映射状态 =====
    /**
     * 映射有效
     */
    public static final Integer MAPPING_VALID = 1;
    
    /**
     * 映射无效
     */
    public static final Integer MAPPING_INVALID = 0;
    
    // ===== 关系状态 =====
    /**
     * 合作中
     */
    public static final Integer COOPERATING = 1;
    
    /**
     * 暂停合作
     */
    public static final Integer COOPERATION_SUSPENDED = 0;
    
    // ===== 工具方法 =====
    
    /**
     * 检查是否为启用状态
     */
    public static boolean isEnabled(Integer status) {
        return status != null && status.equals(ENABLED);
    }
    
    /**
     * 检查是否为禁用状态
     */
    public static boolean isDisabled(Integer status) {
        return status == null || status.equals(DISABLED);
    }
    
    /**
     * 检查是否为有效状态
     */
    public static boolean isValid(Integer status) {
        return status != null && status.equals(VALID);
    }
    
    /**
     * 检查是否为无效状态
     */
    public static boolean isInvalid(Integer status) {
        return status == null || status.equals(INVALID);
    }
    
    /**
     * 检查是否为真
     */
    public static boolean isTrue(Integer value) {
        return value != null && value.equals(TRUE);
    }
    
    /**
     * 检查是否为假
     */
    public static boolean isFalse(Integer value) {
        return value == null || value.equals(FALSE);
    }
    
    /**
     * 获取状态描述
     */
    public static String getStatusDesc(Integer status) {
        if (status == null) return "未知";
        return status.equals(ENABLED) ? "启用" : "禁用";
    }

}
