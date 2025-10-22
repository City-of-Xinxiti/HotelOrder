package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 供应方酒店ID列表响应DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangHotelIdsResponse {

    /**
     * 酒店信息列表
     */
    private List<JinjiangHotelInfo> list;

    /**
     * 页码，从1开始
     */
    private Integer pageNum;

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 酒店信息
     */
    @Data
    public static class JinjiangHotelInfo {

        /**
         * 品牌编码
         */
        private String brandCode;

        /**
         * 酒店编码
         */
        private String innId;
    }
}
