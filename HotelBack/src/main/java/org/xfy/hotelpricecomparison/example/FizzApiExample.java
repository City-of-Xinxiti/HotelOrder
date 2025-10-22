package org.xfy.hotelpricecomparison.example;

import org.xfy.hotelpricecomparison.util.FizzSignatureUtil;

/**
 * Fizz API调用示例
 * 展示如何使用新的API认证方式
 */
public class FizzApiExample {

    public static void main(String[] args) {
        // API配置信息
        String appId = "924497e9-be62-4128-950d-f626657fcd64";
        String key = "65b299b0-27cb-4360-b181-accf1034bef2";
        long timestamp = System.currentTimeMillis();
        
        // 生成签名
        String signature = FizzSignatureUtil.generateSign(appId, String.valueOf(timestamp), key);
        
        // 输出请求头信息
        System.out.println("=== Fizz API 请求头示例 ===");
        System.out.println("fizz-appid: " + appId);
        System.out.println("timestamp: " + timestamp);
        System.out.println("sign: " + signature);
        System.out.println();
        
        // 验证签名
        boolean isValid = FizzSignatureUtil.verifySign(appId, String.valueOf(timestamp), key, signature);
        System.out.println("签名验证结果: " + (isValid ? "通过" : "失败"));
        
        // 展示签名生成过程
        System.out.println("\n=== 签名生成过程 ===");
        String signString = appId + "-" + timestamp + '-' + key;
        System.out.println("原始字符串: " + signString);
        System.out.println("SHA256签名: " + signature);
    }
}
