package org.xfy.hotelpricecomparison.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Fizz API签名工具类
 * 支持新的API认证规范
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
public class FizzSignatureUtil {

    /**
     * 生成Fizz API签名
     * 签名方式：Token = DigestUtils.sha256Hex((appId + "-" + timestamp + '-' + key).getBytes(Charset.forName("UTF-8")));
     * 
     * @param appId 应用ID
     * @param timestamp 时间戳
     * @param key 密钥
     * @return SHA256签名
     */
    public static String generateSign(String appId, String timestamp, String key) {
        try {
            // 构建签名字符串：appId + "-" + timestamp + '-' + key
            String signString = appId + "-" + timestamp + '-' + key;
            
            // 生成SHA256签名
            String signature = FizzSignatureUtil.sha256Hex(signString.getBytes(StandardCharsets.UTF_8));
            
            log.debug("Fizz API签名生成 - 原始字符串: {}, 签名: {}", signString, signature);
            return signature;
            
        } catch (Exception e) {
            log.error("生成Fizz API签名失败", e);
            throw new RuntimeException("生成签名失败", e);
        }
    }

    /**
     * 验证签名是否正确
     * 
     * @param appId 应用ID
     * @param timestamp 时间戳
     * @param key 密钥
     * @param receivedSignature 接收到的签名
     * @return 签名是否正确
     */
    public static boolean verifySign(String appId, String timestamp, String key, String receivedSignature) {
        try {
            String expectedSignature = generateSign(appId, timestamp, key);
            return expectedSignature.equals(receivedSignature);
        } catch (Exception e) {
            log.error("验证Fizz API签名失败", e);
            return false;
        }
    }

    /**
     * SHA256加密
     * 
     * @param input 输入字节数组
     * @return SHA256十六进制字符串
     */
    private static String sha256Hex(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input);
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA256加密失败", e);
            throw new RuntimeException("SHA256加密失败", e);
        }
    }
}
