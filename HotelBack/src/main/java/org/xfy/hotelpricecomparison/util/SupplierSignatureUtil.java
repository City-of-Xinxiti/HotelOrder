package org.xfy.hotelpricecomparison.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 供应方API签名工具类
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
public class SupplierSignatureUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 生成供应方API签名
     * 
     * @param appSecret 应用密钥
     * @param timestamp 时间戳
     * @param request 请求参数
     * @return 签名
     */
    public static String generateSign(String appSecret, String timestamp, Object request) {
        try {
            // 将请求参数转换为JSON字符串
            String requestJson = objectMapper.writeValueAsString(request);
            
            // 构建签名字符串：appSecret + timestamp + requestJson
            String signString = appSecret + timestamp + requestJson;
            
            // 生成MD5签名
            return md5(signString);
            
        } catch (Exception e) {
            log.error("生成供应方API签名失败", e);
            throw new RuntimeException("生成签名失败", e);
        }
    }

    /**
     * MD5加密
     * 
     * @param input 输入字符串
     * @return MD5值
     */
    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5加密失败", e);
            throw new RuntimeException("MD5加密失败", e);
        }
    }
}
