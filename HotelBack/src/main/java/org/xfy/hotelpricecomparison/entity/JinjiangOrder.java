package org.xfy.hotelpricecomparison.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 锦江订单实体类
 * 对应锦江API下单接口
 * 
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jinjiang_orders")
public class JinjiangOrder {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 锦江订单号
     */
    @TableField("order_code")
    private String orderCode;

    /**
     * 渠道（企业方）订单号
     */
    @TableField("external_id")
    private String externalId;

    /**
     * 酒店ID
     */
    @TableField("inn_id")
    private String innId;

    /**
     * 房型编号
     */
    @TableField("room_type_id")
    private String roomTypeId;

    /**
     * 商品编码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 预订人姓名
     */
    @TableField("book_name")
    private String bookName;

    /**
     * 预订人手机
     */
    @TableField("book_mobile")
    private String bookMobile;

    /**
     * 预订人手机国家编码
     */
    @TableField("book_nation_code")
    private String bookNationCode;

    /**
     * 预订人邮箱
     */
    @TableField("book_email")
    private String bookEmail;

    /**
     * 预订人证件号
     */
    @TableField("book_id_card")
    private String bookIdCard;

    /**
     * 预订人证件类型
     */
    @TableField("book_id_card_type")
    private Integer bookIdCardType;

    /**
     * 入住营业日，格式为yyyy-MM-dd
     */
    @TableField("dt_arr_orig")
    private String dtArrOrig;

    /**
     * 离店营业日，格式yyyy-MM-dd
     */
    @TableField("dt_dep_orig")
    private String dtDepOrig;

    /**
     * 到店时间，格式yyyy-MM-dd HH:mm:ss
     */
    @TableField("last_arr_time")
    private String lastArrTime;

    /**
     * 房间数量
     */
    @TableField("room_count")
    private Integer roomCount;

    /**
     * 入住成人数
     */
    @TableField("guests_num")
    private Integer guestsNum;

    /**
     * 支付方式。0：到付，1：预付 2：月结支付 3：企业支付宝代扣
     */
    @TableField("pay_type")
    private Integer payType;

    /**
     * 订单支付总价
     */
    @TableField("total_rate")
    private BigDecimal totalRate;

    /**
     * 订单其他费用
     */
    @TableField("fee_rate")
    private BigDecimal feeRate;

    /**
     * 备注，需在300字符以内
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 订单状态
     */
    @TableField("order_state")
    private Integer orderState;

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

    /**
     * 入住人信息列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<JinjiangPassenger> passengers;
}
