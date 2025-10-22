package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

/**
 * 供应方酒店房型状态请求DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangHotelRoomStatusRequest {

    /**
     * 查询天数(默认1天)
     */
    private Integer days = 1;

    /**
     * 营业日期[endOfDay]不能为空
     */
    private String endOfDay;

    /**
     * 酒店唯一编号
     */
    private String innId;

    /**
     * 1 基础协议价 2 追价(基础协议价+普卡活动价)
     */
    private Integer priceType;

    /**
     * 查询特定商品编码的价格，多个以,号隔开
     */
    private String productCode;

    /**
     * 价格代码
     */
    private String rateCode;

    /**
     * 房型编号(1001 大床房 1002 经济房…)
     */
    private String roomTypeCode;
}