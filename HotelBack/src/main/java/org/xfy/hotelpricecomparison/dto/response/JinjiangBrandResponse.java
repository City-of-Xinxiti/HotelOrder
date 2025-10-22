package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 供应方品牌响应DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangBrandResponse {

    /**
     * 品牌列表
     */
    private List<JinjiangBrand> brand;

    /**
     * 品牌信息
     */
    @Data
    public static class JinjiangBrand {

        /**
         * 品牌唯一编号
         */
        private String brandCode;

        /**
         * 品牌名字
         */
        private String brandName;

        /**
         * 品牌类型(1 简约舒适 2 精品优选 3 豪华高端)
         */
        private Integer brandType;

        /**
         * 品牌描述
         */
        private String description;
    }
}
