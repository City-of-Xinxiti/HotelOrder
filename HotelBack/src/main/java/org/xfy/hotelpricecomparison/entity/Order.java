package org.xfy.hotelpricecomparison.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("orders")
public class Order {

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_number")
    private String orderNumber;

    /**
     * 酒店ID
     */
    @TableField("hotel_id")
    private String hotelId;

    /**
     * 酒店名称
     */
    @TableField("hotel_name")
    private String hotelName;

    /**
     * 房型ID
     */
    @TableField("room_type_id")
    private String roomTypeId;

    /**
     * 房型名称
     */
    @TableField("room_type_name")
    private String roomTypeName;

    /**
     * 入住日期
     */
    @TableField("check_in_date")
    private LocalDate checkInDate;

    /**
     * 离店日期
     */
    @TableField("check_out_date")
    private LocalDate checkOutDate;

    /**
     * 入住晚数
     */
    @TableField("nights")
    private Integer nights;

    /**
     * 入住人数
     */
    @TableField("guests")
    private Integer guests;

    /**
     * 总价格
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

    /**
     * 订单状态：待确认、已确认、已取消
     */
    @TableField("status")
    private String status;

    /**
     * 客户姓名
     */
    @TableField("customer_name")
    private String customerName;

    /**
     * 客户电话
     */
    @TableField("customer_phone")
    private String customerPhone;

    /**
     * 客户邮箱
     */
    @TableField("customer_email")
    private String customerEmail;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    private String customerId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
