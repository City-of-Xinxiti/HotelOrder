package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 供应方酒店ID列表查询请求DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangHotelIdsRequest {

    /**
     * 品牌列表
     */
    private List<String> brandCodes;

    /**
     * 页码，从1开始
     */
    private Integer pageNum = 1;

    /**
     * 页面大小
     */
    private Integer pageSize = 10;
}
