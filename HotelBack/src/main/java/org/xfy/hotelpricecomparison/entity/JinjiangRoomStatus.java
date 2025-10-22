package org.xfy.hotelpricecomparison.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 锦江房态实体类
 * 对应锦江API房态接口
 * 
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jinjiang_room_status")
public class JinjiangRoomStatus {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 酒店ID
     */
    @TableField("inn_id")
    private String innId;

    /**
     * 房型编码
     */
    @TableField("room_type_code")
    private String roomTypeCode;

    /**
     * 房型名称
     */
    @TableField("room_type_name")
    private String roomTypeName;

    /**
     * 房型英文名称
     */
    @TableField("room_type_name_en")
    private String roomTypeNameEn;

    /**
     * 商品编码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 商品类型
     */
    @TableField("product_type")
    private Integer productType;

    /**
     * 协议类型
     */
    @TableField("protocol_type")
    private Integer protocolType;

    /**
     * 营业日期
     */
    @TableField("end_of_day")
    private Long endOfDay;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Long startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Long endTime;

    /**
     * 可售房量
     */
    @TableField("quota")
    private Integer quota;

    /**
     * 入住成人数
     */
    @TableField("guests")
    private Integer guests;

    /**
     * 门市价
     */
    @TableField("rack_rate")
    private BigDecimal rackRate;

    /**
     * 本地预付价格
     */
    @TableField("advance_rate")
    private BigDecimal advanceRate;

    /**
     * 本地现付价
     */
    @TableField("spot_rate")
    private BigDecimal spotRate;

    /**
     * 原预付价
     */
    @TableField("orig_advance_rate")
    private BigDecimal origAdvanceRate;

    /**
     * 原现付价
     */
    @TableField("orig_spot_rate")
    private BigDecimal origSpotRate;

    /**
     * 预付早餐
     */
    @TableField("advance_breakfast_count")
    private Integer advanceBreakfastCount;

    /**
     * 到付早餐
     */
    @TableField("spot_breakfast_count")
    private Integer spotBreakfastCount;

    /**
     * 预付其他费用
     */
    @TableField("advance_fee_rate")
    private BigDecimal advanceFeeRate;

    /**
     * 到付其他费用
     */
    @TableField("spot_fee_rate")
    private BigDecimal spotFeeRate;

    /**
     * 货币代码
     */
    @TableField("currency")
    private String currency;

    /**
     * 是否支持到付和预付
     */
    @TableField("support_pay")
    private String supportPay;

    /**
     * 免费取消时间
     */
    @TableField("free_cancel_time")
    private Long freeCancelTime;

    /**
     * 是否含有生效时间
     */
    @TableField("has_effive_time")
    private Integer hasEffiveTime;

    /**
     * 入住有礼
     */
    @TableField("incremental_preferent")
    private String incrementalPreferent;

    /**
     * 入住有礼开始时间
     */
    @TableField("preferent_start_time")
    private String preferentStartTime;

    /**
     * 入住有礼结束时间
     */
    @TableField("preferent_end_time")
    private String preferentEndTime;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
