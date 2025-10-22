package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 供应方下单预订请求DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangPostOrderRequest {

    /**
     * 预订人邮箱
     */
    private String bookEmail;

    /**
     * 预订人证件号
     */
    private String bookIdCard;

    /**
     * 预订人证件类型
     */
    private Integer bookIdCardType;

    /**
     * 预订人手机
     */
    private String bookMobile;

    /**
     * 预订人手机国家编码
     */
    private String bookNationCode;

    /**
     * 预订人姓名
     */
    private String bookName;

    /**
     * 入住营业日，格式为yyyy-MM-dd
     */
    private String dtArrorig;

    /**
     * 离店营业日，格式yyyy-MM-dd
     */
    private String dtDeporig;

    /**
     * 渠道（企业方）订单号
     */
    private String externalId;

    /**
     * 订单其他费用
     */
    private BigDecimal feeRate;

    /**
     * 入住成人数
     */
    private Integer guestsNum;

    /**
     * 酒店ID
     */
    private String innId;

    /**
     * 到店时间，格式yyyy-MM-dd HH:mm:ss
     */
    private String lastArrTime;

    /**
     * 入住人信息
     */
    private List<JinjiangPassengerRequest> passengers;

    /**
     * 支付方式。0：到付，1：预付 2：月结支付 3：企业支付宝代扣
     */
    private Integer payType;

    /**
     * 商品编码
     */
    private String productCode;

    /**
     * 备注，需在300字符以内
     */
    private String remarks;

    /**
     * 房间数量
     */
    private Integer roomCount;

    /**
     * 房型编号
     */
    private String roomTypeId;

    /**
     * 订单支付总价
     */
    private BigDecimal totalRate;
}
