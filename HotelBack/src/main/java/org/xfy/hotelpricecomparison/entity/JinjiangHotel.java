package org.xfy.hotelpricecomparison.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 锦江酒店实体类
 * 对应锦江API酒店信息接口
 * 
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jinjiang_hotels")
public class JinjiangHotel {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 酒店唯一编号
     */
    @TableField("inn_id")
    private String innId;

    /**
     * 酒店名称
     */
    @TableField("inn_name")
    private String innName;

    /**
     * 酒店英文名称
     */
    @TableField("inn_name_en")
    private String innNameEn;

    /**
     * 酒店短名称
     */
    @TableField("inn_short_name")
    private String innShortName;

    /**
     * 酒店名称全拼
     */
    @TableField("inn_name_pin_yin")
    private String innNamePinYin;

    /**
     * 酒店地址
     */
    @TableField("address")
    private String address;

    /**
     * 城市编号
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 城市名称
     */
    @TableField("city_name")
    private String cityName;

    /**
     * 行政区编号
     */
    @TableField("district_code")
    private String districtCode;

    /**
     * 行政区名称
     */
    @TableField("district_name")
    private String districtName;

    /**
     * 国家编码
     */
    @TableField("country_code")
    private String countryCode;

    /**
     * 酒店电话
     */
    @TableField("inn_phone")
    private String innPhone;

    /**
     * 酒店邮箱
     */
    @TableField("inn_email")
    private String innEmail;

    /**
     * 酒店描述
     */
    @TableField("description")
    private String description;

    /**
     * 预订须知
     */
    @TableField("policy")
    private String policy;

    /**
     * 到店指引
     */
    @TableField("guide")
    private String guide;

    /**
     * 最早入住时间
     */
    @TableField("check_in_time")
    private String checkInTime;

    /**
     * 最晚离店时间
     */
    @TableField("check_out_time")
    private String checkOutTime;

    /**
     * 酒店级别(0 经济型 1 舒适型 2 品质型 3 高档型 4 豪华型)
     */
    @TableField("inn_level")
    private Integer innLevel;

    /**
     * 星级类别(0 一星 1 二星 2 三星 3 四星 4无星 -1无星)
     */
    @TableField("star_type")
    private Integer starType;

    /**
     * 经营类别(0 自营店 1 管理店 2 加盟店 3 标准店 4 合作店)
     */
    @TableField("business_type")
    private Integer businessType;

    /**
     * 酒店类别(100经济型酒店 101 精品商务酒店102 景区度假酒店 103 主题特色酒店 104 民族风情酒店)
     */
    @TableField("inn_type")
    private Integer innType;

    /**
     * 酒店状态(-1 开发 0 筹建中 1开业 2开业后退筹建 3 开业后解约 5 下线整改 6 售出未下 7转品牌 8征用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否有效(1 是 0 否)
     */
    @TableField("valid")
    private Integer valid;

    /**
     * 是否支持预订(1 是 0 否)
     */
    @TableField("book_flag")
    private Integer bookFlag;

    /**
     * 是否允许未成年人入住(1 是 0 否)
     */
    @TableField("allow_minors")
    private Integer allowMinors;

    /**
     * 是否允许携带宠物(1 是 0 否)
     */
    @TableField("pets")
    private Integer pets;

    /**
     * 是否有餐厅(1 是 0 否)
     */
    @TableField("restaurant")
    private Integer restaurant;

    /**
     * 是否可接待外宾(1、可接待 0 不可接待)
     */
    @TableField("support_foreign_guest")
    private Integer supportForeignGuest;

    /**
     * 英文姓名是否必填(1 是 0 否)
     */
    @TableField("require_eng_name")
    private Integer requireEngName;

    /**
     * 可接受证件类型
     */
    @TableField("certificate_type")
    private String certificateType;

    /**
     * 结算货币代码
     */
    @TableField("settlement_currency")
    private String settlementCurrency;

    /**
     * 酒店来源
     */
    @TableField("source_type")
    private String sourceType;

    /**
     * 开业时间
     */
    @TableField("open_date")
    private Long openDate;

    /**
     * 停业时间
     */
    @TableField("close_date")
    private Long closeDate;

    /**
     * 装修时间
     */
    @TableField("decoration_date")
    private String decorationDate;

    /**
     * 是否高星酒店(值1为是，其它值为否)
     */
    @TableField("is_jin_rewards")
    private String isJinRewards;

    /**
     * 品牌编号
     */
    @TableField("brand_code")
    private String brandCode;

    /**
     * 语言类型(0 中文 1 英文)
     */
    @TableField("language_code")
    private Integer languageCode;

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
