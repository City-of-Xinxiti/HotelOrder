package org.xfy.hotelpricecomparison.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.xfy.hotelpricecomparison.dto.request.*;
import org.xfy.hotelpricecomparison.dto.response.*;
import org.xfy.hotelpricecomparison.service.JinjiangApiService;
import org.xfy.hotelpricecomparison.util.FizzSignatureUtil;
import org.xfy.hotelpricecomparison.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 锦江API集成服务实现类
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
@Service
public class JinjiangApiServiceImpl implements JinjiangApiService, InitializingBean {

    @Value("${jinjiang.api.base-url}")
    private String baseUrl;

    @Value("${jinjiang.api.app-id}")
    private String appId;

    @Value("${jinjiang.api.app-secret}")
    private String appSecret;
    
    // 超时设置
    @Value("${jinjiang.api.connect-timeout:5000}")
    private int connectTimeout;
    
    @Value("${jinjiang.api.read-timeout:10000}")
    private int readTimeout;
    
    // 使用构造函数注入RestTemplate
    private final RestTemplate restTemplate;
    
    // 构造函数
    public JinjiangApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    // 实现InitializingBean接口的afterPropertiesSet方法
    @Override
    public void afterPropertiesSet() {
        configureRestTemplate();
    }
    
    /**
     * 配置RestTemplate超时参数
     */
    private void configureRestTemplate() {
        ClientHttpRequestFactory factory = restTemplate.getRequestFactory();
        if (factory instanceof SimpleClientHttpRequestFactory) {
            SimpleClientHttpRequestFactory clientHttpRequestFactory = (SimpleClientHttpRequestFactory) factory;
            clientHttpRequestFactory.setConnectTimeout(connectTimeout);
            clientHttpRequestFactory.setReadTimeout(readTimeout);
            log.info("RestTemplate超时配置已设置 - 连接超时: {}ms, 读取超时: {}ms", connectTimeout, readTimeout);
        }
    }

    /**
     * 获取品牌列表
     */
    @Override
    public JinjiangApiResponse<JinjiangBrandResponse> getBrandList(JinjiangBrandListRequest request) {
        String url = baseUrl + "/brand/getBrandList";
        log.debug("开始调用供应商API获取品牌列表");
        
        // 调用API获取品牌列表
        JinjiangApiResponse<JinjiangBrandResponse> response = callSupplierApi(url, request, JinjiangBrandResponse.class);
        
        // 如果成功获取到数据，只保留第一个品牌
        if (response != null && response.getResult() != null && response.getResult().getBrand() != null) {
            JinjiangBrandResponse result = response.getResult();
            if (result.getBrand().size() > 1) {
                log.info("获取到{}个品牌，只保留第一个品牌", result.getBrand().size());
                result.setBrand(result.getBrand().subList(0, 1));
            }
        }
        
        return response;
    }

    @Override
    public JinjiangApiResponse<JinjiangHotelIdsResponse> getHotelIds(JinjiangHotelIdsRequest request) {
        String url = baseUrl + "/hotel/getHotelIds";
        log.debug("开始调用供应商API获取酒店ID列表 - 页码: {}, 每页数量: {}", request.getPageNum(), request.getPageSize());
        
        // 调用API获取酒店列表
        JinjiangApiResponse<JinjiangHotelIdsResponse> response = callSupplierApi(url, request, JinjiangHotelIdsResponse.class);
        
        // 如果成功获取到数据，只保留前10个酒店
        if (response != null && response.getResult() != null && response.getResult().getList() != null) {
            JinjiangHotelIdsResponse result = response.getResult();
            if (result.getList().size() > 10) {
                log.info("获取到{}个酒店，只保留前10个酒店", result.getList().size());
                result.setList(result.getList().subList(0, 10));
                result.setTotal(10);
                result.setPages(1);
            }
        }
        
        return response;
    }

    @Override
    public JinjiangApiResponse<JinjiangHotelInfoResponse> getHotelInfo(JinjiangHotelInfoRequest request) {
        String url = baseUrl + "/hotel/getHotelInfo";
        log.debug("开始调用供应商API获取酒店信息 - 酒店ID: {}", request.getInnId());
        log.info("注意：此方法只处理前10个酒店的信息");
        return callSupplierApi(url, request, JinjiangHotelInfoResponse.class);
    }

    @Override
    public JinjiangApiResponse<JinjiangHotelRoomTypeResponse> getHotelRoomType(JinjiangHotelRoomTypeRequest request) {
        String url = baseUrl + "/hotel/getHotelRoomType";
        log.debug("开始调用供应商API获取酒店房型信息 - 酒店ID: {}", request.getHotelId());
        log.info("注意：此方法只处理前10个酒店的房型信息");
        return callSupplierApi(url, request, JinjiangHotelRoomTypeResponse.class);
    }

    @Override
    public JinjiangApiResponse<JinjiangHotelRoomStatusResponse> getHotelRealRoomStatus(JinjiangHotelRoomStatusRequest request) {
        String url = baseUrl + "/hotel/getHotelRealRoomStatus";
        log.debug("开始调用供应商API获取酒店实时房态 - 酒店ID: {}", request.getInnId());
        log.info("注意：此方法只处理前10个酒店的房态信息");
        return callSupplierApi(url, request, JinjiangHotelRoomStatusResponse.class);
    }


    @Override
    public JinjiangApiResponse<JinjiangPostOrderResponse> postOrder(JinjiangPostOrderRequest request) {
        String url = baseUrl + "/booking/postOrder";
        log.info("开始调用供应商API提交订单");
        return callSupplierApi(url, request, JinjiangPostOrderResponse.class);
    }

    @Override
    public JinjiangApiResponse<JinjiangCancelOrderResponse> cancelOrder(JinjiangCancelOrderRequest request) {
        String url = baseUrl + "/booking/cancelOrder";
        log.info("开始调用供应商API取消订单 - 订单号: {}", request.getOrderCode());
        return callSupplierApi(url, request, JinjiangCancelOrderResponse.class);
    }


    /**
     * 调用锦江API的通用方法
     */
    private <T> JinjiangApiResponse<T> callSupplierApi(String url, Object request, Class<T> responseType) {
        try {
            LogUtil.logApiRequest(url, request);
            
            // 参数验证
            validateRequest(request);
            
            // 生成时间戳和签名
            String timestamp = String.valueOf(System.currentTimeMillis());
            String signature = FizzSignatureUtil.generateSign(appId, timestamp, appSecret);
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("fizz-appid", appId);
            headers.set("timestamp", timestamp);
            headers.set("sign", signature);
            
            // 创建请求实体 - 将请求参数包装在param字段中
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("param", request);
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 发送HTTP请求并获取原始响应
            log.debug("发送API请求到: {}", url);
            org.springframework.http.ResponseEntity<Object> responseEntity = 
                restTemplate.postForEntity(url, requestEntity, Object.class);
            
            // 检查响应状态
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Object responseBody = responseEntity.getBody();
                LogUtil.logApiResponse(url, responseBody);
                
                // 检查响应内容类型
                org.springframework.http.HttpHeaders responseHeaders = responseEntity.getHeaders();
                String contentType = responseHeaders.getContentType() != null ? 
                    responseHeaders.getContentType().toString() : "unknown";
                
                if (contentType.contains("application/json")) {
                    // JSON响应，正常解析
                    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    
                    // 将响应体转换为JsonNode
                    com.fasterxml.jackson.databind.JsonNode jsonNode;
                    if (responseBody instanceof String) {
                        jsonNode = objectMapper.readTree((String) responseBody);
                    } else {
                        // 如果已经是对象，直接转换
                        jsonNode = objectMapper.valueToTree(responseBody);
                    }
                    
                    JinjiangApiResponse<T> response = new JinjiangApiResponse<>();
                    
                    // 设置基本字段
                    if (jsonNode.has("msgCode")) {
                        response.setMsgCode(jsonNode.get("msgCode").asInt());
                    }
                    if (jsonNode.has("message")) {
                        response.setMessage(jsonNode.get("message").asText());
                    }
                    if (jsonNode.has("errors")) {
                        response.setErrors(jsonNode.get("errors"));
                    }
                    if (jsonNode.has("result")) {
                        // 解析result字段为指定的类型
                        com.fasterxml.jackson.databind.JsonNode resultNode = jsonNode.get("result");
                        if (resultNode != null && !resultNode.isNull()) {
                            T result = objectMapper.treeToValue(resultNode, responseType);
                            response.setResult(result);
                        }
                    }
                    
                    // 请求结果验证
                    if (!validateResponse(response, responseType)) {
                        log.warn("API响应验证失败: {}", url);
                        return createEmptyResponse(responseType);
                    }
                    
                    log.debug("API调用成功: {}, 响应状态码: {}", url, response.getMsgCode());
                    LogUtil.logApiResponse(url + " (解析后)", response);
                    return response;
                } else {
                    // 非JSON响应处理
                    log.error("API返回非JSON响应，URL: {}, 内容类型: {}, 响应内容: {}", 
                        url, contentType, responseBody);
                    // 对于静态数据，返回空的默认响应而不是抛出异常
                    return createEmptyResponse(responseType);
                }
            } else {
                // 增强的错误处理
                handleNonSuccessResponse(url, responseEntity);
                // 如果是非2xx状态码，抛出异常触发重试
                throw new RuntimeException(String.format("API调用失败，URL: %s, 状态码: %s", 
                    url, responseEntity.getStatusCode()));
            }

        } catch (HttpClientErrorException e) {
            // 客户端错误处理 (4xx)
            log.error("HTTP客户端错误 - URL: {}, 状态码: {}, 错误信息: {}", 
                url, e.getStatusCode(), e.getResponseBodyAsString(), e);
            return createErrorResponse(responseType, 400, "请求参数错误: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            // 服务端错误处理 (5xx)
            log.error("HTTP服务端错误 - URL: {}, 状态码: {}, 错误信息: {}", 
                url, e.getStatusCode(), e.getResponseBodyAsString(), e);
            // 服务端错误可以重试
            throw e;
        } catch (RestClientException e) {
            // 网络相关异常处理
            log.error("网络请求异常 - URL: {}, 错误信息: {}", url, e.getMessage(), e);
            // 网络异常可以重试
            throw e;
        } catch (Exception e) {
            // 其他异常处理
            LogUtil.logError("调用锦江API", "URL: " + url + " - API调用失败: " + e.getMessage(), e);
            // 对于静态数据，返回空的默认响应以提高系统健壮性
            return createEmptyResponse(responseType);
        }
    }
    
    /**
     * 处理非成功响应状态码
     */
    private void handleNonSuccessResponse(String url, org.springframework.http.ResponseEntity<Object> responseEntity) {
        try {
            Object responseBody = responseEntity.getBody();
            String responseDetails = responseBody != null ? responseBody.toString() : "无响应内容";
            log.error("API调用失败 - URL: {}, 状态码: {}, 响应内容: {}", 
                url, responseEntity.getStatusCode(), responseDetails);
            
            // 根据不同的状态码提供更详细的错误信息
            if (responseEntity.getStatusCode().is4xxClientError()) {
                log.warn("客户端错误，可能是请求参数问题或权限不足");
            } else if (responseEntity.getStatusCode().is5xxServerError()) {
                log.warn("服务端错误，远程服务暂时不可用，将触发重试机制");
            }
        } catch (Exception e) {
            log.error("处理非成功响应时出错", e);
        }
    }
    
    /**
     * 验证响应结果
     */
    private <T> boolean validateResponse(JinjiangApiResponse<T> response, Class<T> responseType) {
        // 基础验证
        if (response == null) {
            log.error("响应对象为空");
            return false;
        }
        
        // 验证状态码
        if (response.getMsgCode() != null && response.getMsgCode() != 0) {
            log.warn("API返回非成功状态码: {}, 消息: {}", response.getMsgCode(), response.getMessage());
            // 对于某些特殊错误码，可以根据业务需求决定是否返回false
        }
        
        // 对不同类型的响应进行特定验证
        if (responseType == JinjiangBrandResponse.class) {
            return validateBrandResponse(response);
        } else if (responseType == JinjiangHotelIdsResponse.class) {
            return validateHotelIdsResponse(response);
        } else if (responseType == JinjiangHotelInfoResponse.class) {
            return validateHotelInfoResponse(response);
        }
        
        // 默认通过验证
        return true;
    }
    
    /**
     * 验证品牌响应
     */
    private <T> boolean validateBrandResponse(JinjiangApiResponse<T> response) {
        if (response.getResult() == null) {
            return false;
        }
        
        try {
            JinjiangBrandResponse brandResponse = (JinjiangBrandResponse) response.getResult();
            // 品牌列表可以为空，但result对象必须存在
            return brandResponse != null;
        } catch (ClassCastException e) {
            log.error("品牌响应类型转换失败", e);
            return false;
        }
    }
    
    /**
     * 验证酒店ID列表响应
     */
    private <T> boolean validateHotelIdsResponse(JinjiangApiResponse<T> response) {
        if (response.getResult() == null) {
            return false;
        }
        
        try {
            JinjiangHotelIdsResponse hotelIdsResponse = (JinjiangHotelIdsResponse) response.getResult();
            // 酒店ID列表可以为空，但result对象必须存在
            return hotelIdsResponse != null;
        } catch (ClassCastException e) {
            log.error("酒店ID列表响应类型转换失败", e);
            return false;
        }
    }
    
    /**
     * 验证酒店信息响应
     */
    private <T> boolean validateHotelInfoResponse(JinjiangApiResponse<T> response) {
        if (response.getResult() == null) {
            return false;
        }
        
        try {
            JinjiangHotelInfoResponse hotelInfoResponse = (JinjiangHotelInfoResponse) response.getResult();
            // 酒店信息响应必须包含基本的酒店信息
            return hotelInfoResponse != null;
        } catch (ClassCastException e) {
            log.error("酒店信息响应类型转换失败", e);
            return false;
        }
    }
    
    /**
     * 创建错误响应
     */
    private <T> JinjiangApiResponse<T> createErrorResponse(Class<T> responseType, int errorCode, String errorMessage) {
        JinjiangApiResponse<T> response = new JinjiangApiResponse<>();
        response.setMsgCode(errorCode);
        response.setMessage(errorMessage);
        
        // 如果是品牌响应，创建空的品牌列表
        if (responseType == JinjiangBrandResponse.class) {
            try {
                JinjiangBrandResponse brandResponse = new JinjiangBrandResponse();
                brandResponse.setBrand(java.util.Collections.emptyList()); // 空品牌列表
                response.setResult((T) brandResponse);
            } catch (Exception e) {
                log.error("创建错误品牌响应失败", e);
            }
        }
        
        return response;
    }
    
    /**
     * 验证请求参数
     */
    private void validateRequest(Object request) {
        if (request == null) {
            log.warn("请求参数为空");
            return;
        }
        
        // 对品牌列表请求进行特殊验证
        if (request instanceof JinjiangBrandListRequest) {
            JinjiangBrandListRequest brandRequest = (JinjiangBrandListRequest) request;
            // 品牌编码长度限制
            if (brandRequest.getBrandCode() != null) {
                if (brandRequest.getBrandCode().length() > 50) {
                    log.warn("品牌编码长度超过限制: {}", brandRequest.getBrandCode());
                } else if (brandRequest.getBrandCode().trim().isEmpty()) {
                    log.warn("品牌编码为空字符串");
                }
            }
            // 语言代码有效性检查
            if (brandRequest.getLanguageCode() != null && 
                (brandRequest.getLanguageCode() < 0 || brandRequest.getLanguageCode() > 1)) {
                log.warn("无效的语言代码: {}", brandRequest.getLanguageCode());
            }
        }
        // 对酒店ID请求进行验证
        else if (request instanceof JinjiangHotelIdsRequest) {
            JinjiangHotelIdsRequest hotelIdsRequest = (JinjiangHotelIdsRequest) request;
            if (hotelIdsRequest.getPageNum() == null || hotelIdsRequest.getPageNum() < 1) {
                log.warn("无效的页码: {}", hotelIdsRequest.getPageNum());
            }
            if (hotelIdsRequest.getPageSize() == null || hotelIdsRequest.getPageSize() < 1 || hotelIdsRequest.getPageSize() > 1000) {
                log.warn("无效的每页数量: {}", hotelIdsRequest.getPageSize());
            }
        }
        // 对酒店信息请求进行验证
        else if (request instanceof JinjiangHotelInfoRequest) {
            log.debug("验证酒店信息请求参数");
        }
        // 对酒店房态请求进行验证
        else if (request instanceof JinjiangHotelRoomStatusRequest) {
            JinjiangHotelRoomStatusRequest roomStatusRequest = (JinjiangHotelRoomStatusRequest) request;
            if (roomStatusRequest.getInnId() == null || roomStatusRequest.getInnId().trim().isEmpty()) {
                log.warn("酒店ID为空");
            }
            if (roomStatusRequest.getDays() == null || roomStatusRequest.getDays() < 1 || roomStatusRequest.getDays() > 90) {
                log.warn("无效的查询天数: {}", roomStatusRequest.getDays());
            }
        }
    }
    
    /**
     * 创建空的默认响应，用于降级处理
     */
    private <T> JinjiangApiResponse<T> createEmptyResponse(Class<T> responseType) {
        JinjiangApiResponse<T> response = new JinjiangApiResponse<>();
        response.setMsgCode(500); // 设置错误状态码
        response.setMessage("服务暂时不可用，返回默认数据");
        
        // 根据不同的响应类型创建对应的空响应
        if (responseType == JinjiangBrandResponse.class) {
            try {
                JinjiangBrandResponse brandResponse = new JinjiangBrandResponse();
                brandResponse.setBrand(java.util.Collections.emptyList()); // 空品牌列表
                response.setResult((T) brandResponse);
            } catch (Exception e) {
                log.error("创建默认品牌响应失败", e);
            }
        } else if (responseType == JinjiangHotelIdsResponse.class) {
            try {
                JinjiangHotelIdsResponse hotelIdsResponse = new JinjiangHotelIdsResponse();
                hotelIdsResponse.setList(java.util.Collections.emptyList());
                hotelIdsResponse.setTotal(0);
                response.setResult((T) hotelIdsResponse);
            } catch (Exception e) {
                log.error("创建默认酒店ID列表响应失败", e);
            }
        }
        
        return response;
    }
    
    /**
     * 获取酒店图片
     */
    @Override
    public JinjiangApiResponse<JinjiangHotelImageResponse> getHotelImages(JinjiangHotelImageRequest request) {
        try {
            log.info("开始获取酒店图片，酒店ID: {}, 语言类型: {}", request.getInnId(), request.getLangType());
            
            // 参数验证
            if (request.getInnId() == null || request.getInnId().trim().isEmpty()) {
                log.warn("酒店ID为空");
                return createEmptyResponse(JinjiangHotelImageResponse.class);
            }
            
            // 生成时间戳和签名
            String timestamp = String.valueOf(System.currentTimeMillis());
            String signature = FizzSignatureUtil.generateSign(appId, timestamp, appSecret);
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("fizz-appid", appId);
            headers.set("timestamp", timestamp);
            headers.set("sign", signature);
            
            // 创建请求实体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("param", request);
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 调用锦江API
            String url = baseUrl + "/hotel/getHotelImage";
            log.debug("调用锦江API获取酒店图片: {}", url);
            
            JinjiangApiResponse<JinjiangHotelImageResponse> response = restTemplate.postForObject(
                url, 
                requestEntity, 
                JinjiangApiResponse.class
            );
            
            if (response != null) {
                log.info("获取酒店图片成功，返回数据: {}", response);
            } else {
                log.warn("获取酒店图片返回空响应");
            }
            
            return response;
            
        } catch (HttpClientErrorException e) {
            log.error("获取酒店图片HTTP客户端错误: {}", e.getMessage());
            return createEmptyResponse(JinjiangHotelImageResponse.class);
        } catch (HttpServerErrorException e) {
            log.error("获取酒店图片HTTP服务器错误: {}", e.getMessage());
            return createEmptyResponse(JinjiangHotelImageResponse.class);
        } catch (RestClientException e) {
            log.error("获取酒店图片网络错误: {}", e.getMessage());
            return createEmptyResponse(JinjiangHotelImageResponse.class);
        } catch (Exception e) {
            log.error("获取酒店图片未知错误", e);
            return createEmptyResponse(JinjiangHotelImageResponse.class);
        }
    }
}
