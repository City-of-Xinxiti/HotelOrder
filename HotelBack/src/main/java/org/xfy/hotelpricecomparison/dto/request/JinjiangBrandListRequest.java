package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

/**
 * 供应方品牌列表查询请求DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangBrandListRequest {

    /**
     * 品牌唯一编号
     */
    private String brandCode;

    /**
     * 语言类型(0 中文 1 英文)，默认0
     */
    private Integer languageCode = 0;
}
