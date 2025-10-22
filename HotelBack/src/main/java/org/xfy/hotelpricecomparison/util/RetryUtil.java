package org.xfy.hotelpricecomparison.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * 重试工具类
 * 提供通用的重试机制
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
public class RetryUtil {

    /**
     * 默认重试次数
     */
    private static final int DEFAULT_MAX_RETRIES = 3;

    /**
     * 默认重试间隔（毫秒）
     */
    private static final long DEFAULT_RETRY_DELAY = 1000;

    /**
     * 执行带重试的操作
     * 
     * @param operation 操作
     * @param operationName 操作名称
     * @return 操作结果
     */
    public static <T> T executeWithRetry(Supplier<T> operation, String operationName) {
        return executeWithRetry(operation, operationName, DEFAULT_MAX_RETRIES, DEFAULT_RETRY_DELAY);
    }

    /**
     * 执行带重试的操作
     * 
     * @param operation 操作
     * @param operationName 操作名称
     * @param maxRetries 最大重试次数
     * @param retryDelay 重试间隔（毫秒）
     * @return 操作结果
     */
    public static <T> T executeWithRetry(Supplier<T> operation, String operationName, 
                                       int maxRetries, long retryDelay) {
        int retryCount = 0;
        Exception lastException = null;

        while (retryCount <= maxRetries) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                retryCount++;

                if (retryCount > maxRetries) {
                    log.error("{} 重试 {} 次后仍然失败: {}", operationName, maxRetries, e.getMessage(), e);
                    throw new RuntimeException(operationName + " 重试失败: " + e.getMessage(), e);
                } else {
                    log.warn("{} 第 {} 次尝试失败，{}ms后重试: {}", 
                        operationName, retryCount, retryDelay, e.getMessage());
                    
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("重试等待被中断", ie);
                        throw new RuntimeException("重试等待被中断", ie);
                    }
                }
            }
        }

        throw new RuntimeException(operationName + " 执行失败", lastException);
    }

    /**
     * 执行带重试的操作（无返回值）
     * 
     * @param operation 操作
     * @param operationName 操作名称
     */
    public static void executeWithRetry(Runnable operation, String operationName) {
        executeWithRetry(operation, operationName, DEFAULT_MAX_RETRIES, DEFAULT_RETRY_DELAY);
    }

    /**
     * 执行带重试的操作（无返回值）
     * 
     * @param operation 操作
     * @param operationName 操作名称
     * @param maxRetries 最大重试次数
     * @param retryDelay 重试间隔（毫秒）
     */
    public static void executeWithRetry(Runnable operation, String operationName, 
                                       int maxRetries, long retryDelay) {
        executeWithRetry(() -> {
            operation.run();
            return null;
        }, operationName, maxRetries, retryDelay);
    }
}
