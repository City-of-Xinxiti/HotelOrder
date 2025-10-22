package org.xfy.hotelpricecomparison.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 日志工具类
 * 提供安全的日志记录功能，过滤敏感信息
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
public class LogUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 敏感字段集合
     */
    private static final Set<String> SENSITIVE_FIELDS = Set.of(
        "password", "pwd", "secret", "token", "key", "signature", "sign",
        "appSecret", "appKey", "accessToken", "refreshToken", "authorization"
    );

    /**
     * 安全地记录API请求日志
     * 
     * @param url API地址
     * @param request 请求对象
     */
    public static void logApiRequest(String url, Object request) {
        try {
            String safeRequest = sanitizeObject(request);
            log.info("调用API: {}, 请求参数: {}", url, safeRequest);
        } catch (Exception e) {
            log.warn("记录API请求日志失败: {}", e.getMessage());
            log.info("调用API: {}", url);
        }
    }

    /**
     * 安全地记录API响应日志
     * 
     * @param url API地址
     * @param response 响应对象
     */
    public static void logApiResponse(String url, Object response) {
        try {
            String safeResponse = sanitizeObject(response);
            log.info("API响应: {}, 响应内容: {}", url, safeResponse);
        } catch (Exception e) {
            log.warn("记录API响应日志失败: {}", e.getMessage());
            log.info("API响应: {}", url);
        }
    }

    /**
     * 安全地记录错误日志
     * 
     * @param operation 操作名称
     * @param error 错误信息
     * @param throwable 异常对象
     */
    public static void logError(String operation, String error, Throwable throwable) {
        log.error("{} 失败: {}", operation, error, throwable);
    }

    /**
     * 安全地记录警告日志
     * 
     * @param operation 操作名称
     * @param message 警告信息
     */
    public static void logWarning(String operation, String message) {
        log.warn("{} 警告: {}", operation, message);
    }

    /**
     * 安全地记录信息日志
     * 
     * @param operation 操作名称
     * @param message 信息内容
     */
    public static void logInfo(String operation, String message) {
        log.info("{}: {}", operation, message);
    }

    /**
     * 清理对象中的敏感信息
     * 
     * @param obj 要清理的对象
     * @return 清理后的JSON字符串
     */
    private static String sanitizeObject(Object obj) {
        if (obj == null) {
            return "null";
        }

        try {
            // 将对象转换为JsonNode
            ObjectNode jsonNode = objectMapper.valueToTree(obj);
            
            // 递归清理敏感字段
            sanitizeJsonNode(jsonNode);
            
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            log.warn("清理敏感信息失败: {}", e.getMessage());
            return "[数据清理失败]";
        }
    }

    /**
     * 递归清理JsonNode中的敏感字段
     * 
     * @param node JsonNode对象
     */
    private static void sanitizeJsonNode(com.fasterxml.jackson.databind.JsonNode node) {
        if (node == null || !node.isObject()) {
            return;
        }

        ObjectNode objectNode = (ObjectNode) node;
        
        // 遍历所有字段
        objectNode.fieldNames().forEachRemaining(fieldName -> {
            if (isSensitiveField(fieldName)) {
                // 替换敏感字段值为星号
                objectNode.put(fieldName, "***");
            } else if (objectNode.get(fieldName).isObject()) {
                // 递归处理嵌套对象
                sanitizeJsonNode(objectNode.get(fieldName));
            }
        });
    }

    /**
     * 检查字段名是否为敏感字段
     * 
     * @param fieldName 字段名
     * @return 是否为敏感字段
     */
    private static boolean isSensitiveField(String fieldName) {
        if (fieldName == null) {
            return false;
        }
        
        String lowerFieldName = fieldName.toLowerCase();
        return SENSITIVE_FIELDS.stream()
            .anyMatch(sensitiveField -> lowerFieldName.contains(sensitiveField.toLowerCase()));
    }

    /**
     * 私有构造函数，防止实例化
     */
    private LogUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
}
