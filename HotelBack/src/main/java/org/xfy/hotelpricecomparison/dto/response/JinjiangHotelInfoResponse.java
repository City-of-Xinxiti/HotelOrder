package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 供应方酒店信息响应DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangHotelInfoResponse {

    /**
     * 酒店地址
     */
    private String address;

    /**
     * 是否允许未成年人入住(1 是 0 否)
     */
    private Integer allowMinors;

    /**
     * 是否支持预订(1 是 0 否)
     */
    private Integer bookFlag;

    /**
     * 品牌编号
     */
    private String brandCode;

    /**
     * 经营类别(0 自营店 1 管理店 2 加盟店 3 标准店 4 合作店)
     */
    private Integer businessType;

    /**
     * 酒店前台可接受证件
     */
    private List<Integer> certificateType;

    /**
     * 最早入住时间
     */
    private String checkInTime;

    /**
     * 最晚离店时间
     */
    private String checkOutTime;

    /**
     * 城市编号
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 停业时间
     */
    private Long closeDate;

    /**
     * 国家编码
     */
    private String countryCode;

    /**
     * 装修时间
     */
    private String decorationDate;

    /**
     * 描述信息(富文本)
     */
    private String description;

    /**
     * 行政区编号
     */
    private String districtCode;

    /**
     * 行政区名称
     */
    private String districtName;

    /**
     * 酒店设施列表
     */
    private List<FacilityInfo> facList;

    /**
     * 到店指引
     */
    private String guide;

    /**
     * 酒店邮箱
     */
    private String innEmail;

    /**
     * 酒店唯一编号
     */
    private String innId;

    /**
     * 酒店级别(0 经济型 1 舒适型 2 品质型 3 高档型 4 豪华型)
     */
    private Integer innLevel;

    /**
     * 酒店名称
     */
    private String innName;

    /**
     * 酒店英文名称
     */
    private String innNameEn;

    /**
     * 酒店名称全拼
     */
    private String innNamePinYin;

    /**
     * 酒店电话
     */
    private String innPhone;

    /**
     * 酒店短名称
     */
    private String innShortName;

    /**
     * 酒店类别(100经济型酒店 101 精品商务酒店102 景区度假酒店 103 主题特色酒店 104 民族风情酒店)
     */
    private Integer innType;

    /**
     * 发票类型
     */
    private List<Integer> invoiceType;

    /**
     * 酒店地图坐标信息
     */
    private List<MapInfo> mapInfo;

    /**
     * 开业时间
     */
    private Long openDate;

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
     * 是否允许携带宠物(1 是 0 否)
     */
    private Integer pets;

    /**
     * 预订须知
     */
    private String policy;

    /**
     * 英文姓名是否必填(1 是 0 否)
     */
    private Integer requireEngName;

    /**
     * 是否有餐厅(1 是 0 否)
     */
    private Integer restaurant;

    /**
     * 酒店服务列表
     */
    private List<ServiceInfo> serList;

    /**
     * 结算货币代码
     */
    private String settlementCurrency;

    /**
     * 酒店来源
     */
    private String sourceType;

    /**
     * 特色服务列表
     */
    private List<SpecialServiceInfo> spList;

    /**
     * 星级类别(0 一星 1 二星 2 三星 3 四星 4无星 -1无星)，已弃用
     */
    private Integer starType;

    /**
     * 酒店状态(-1 开发 0 筹建中 1开业 2开业后退筹建 3 开业后解约 5 下线整改 6 售出未下 7转品牌 8征用
     */
    private Integer status;

    /**
     * 1、可接待 0 不可接待 可以为空
     */
    private Integer supportForeignGuest;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 是否有效(1 是 0 否)
     */
    private Integer valid;

    /**
     * 是否高星酒店(值1为是，其它值为否)
     */
    private String isJinRewards;

    /**
     * 酒店设施信息
     */
    @Data
    public static class FacilityInfo {
        /**
         * 设施编码
         */
        private String facCode;

        /**
         * 设施名称
         */
        private String facName;

        /**
         * 是否有效(1 有效 0 无效)
         */
        private Integer valid;
    }

    /**
     * 酒店地图坐标信息
     */
    @Data
    public static class MapInfo {
        /**
         * 纬度
         */
        private Double lag;

        /**
         * 经度
         */
        private Double lng;

        /**
         * 坐标类型(0 百度 1 Google 2 腾讯 3 高德)
         */
        private Integer mapType;
    }

    /**
     * 酒店服务信息
     */
    @Data
    public static class ServiceInfo {
        /**
         * 服务编码
         */
        private String serCode;

        /**
         * 服务名称
         */
        private String serName;

        /**
         * 是否有效(1 有效 0 无效)
         */
        private Integer valid;
    }

    /**
     * 特色服务信息
     */
    @Data
    public static class SpecialServiceInfo {
        /**
         * 特色服务编码
         */
        private String spCode;

        /**
         * 特色服务名称
         */
        private String spName;

        /**
         * 是否有效(1 有效 0 无效)
         */
        private Integer valid;
    }
}