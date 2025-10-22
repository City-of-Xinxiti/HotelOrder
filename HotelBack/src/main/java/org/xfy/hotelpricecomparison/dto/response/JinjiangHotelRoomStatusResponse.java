package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 供应方酒店房态响应DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangHotelRoomStatusResponse {

    /**
     * 房型列表
     */
    private List<JinjiangRoomTypeList> roomTypeList;

    /**
     * 房型列表
     */
    @Data
    public static class JinjiangRoomTypeList {

        /**
         * 房型编码
         */
        private String roomTypeCode;

        /**
         * 房型名称
         */
        private String roomTypeName;

        /**
         * 房型英文名称
         */
        private String roomTypeNameEn;

        /**
         * 商品列表
         */
        private List<JinjiangProduct> productList;
    }

    /**
     * 商品信息
     */
    @Data
    public static class JinjiangProduct {

        /**
         * 预付早餐
         */
        private Integer advanceBreakfastCount;

        /**
         * 预付其他费用
         */
        private BigDecimal advanceFeeRate;

        /**
         * 本地预付价格
         */
        private BigDecimal advanceRate;

        /**
         * 预定规则
         */
        private JinjiangBookRule bookRule;

        /**
         * 取消规则
         */
        private JinjiangCancelRule cancelRule;

        /**
         * 货币代码
         */
        private String currency;

        /**
         * 营业日期
         */
        private Long endOfDay;

        /**
         * 结束时间
         */
        private Long endTime;

        /**
         * 免费取消时间
         */
        private Long freeCancelTime;

        /**
         * 入住成人数
         */
        private Integer guests;

        /**
         * 是否含有生效时间
         */
        private Boolean hasEffiveTime;

        /**
         * 入住有礼
         */
        private String incrementalPreferent;

        /**
         * 酒店当地货币价格
         */
        private BigDecimal localRate;

        /**
         * 入住有礼详情
         */
        private JinjiangPreferentInfo preferentInfo;

        /**
         * 入住有礼结束时间
         */
        private String preferentEndTime;

        /**
         * 入住有礼开始时间
         */
        private String preferentStartTime;

        /**
         * 原预付价
         */
        private BigDecimal origAdvanceRate;

        /**
         * 原现付价
         */
        private BigDecimal origSpotRate;

        /**
         * 商品编号
         */
        private String productCode;

        /**
         * 商品名称
         */
        private String productName;

        /**
         * 商品类型
         */
        private Integer productType;

        /**
         * 协议类型
         */
        private Integer protocolType;


        /**
         * 可售房量
         */
        private Integer quota;

        /**
         * 门市价
         */
        private BigDecimal rackRate;

        /**
         * 到付早餐
         */
        private Integer spotBreakfastCount;

        /**
         * 到付其他费用
         */
        private BigDecimal spotFeeRate;

        /**
         * 本地现付价
         */
        private BigDecimal spotRate;

        /**
         * 开始时间
         */
        private Long startTime;

        /**
         * 是否支持到付和预付
         */
        private List<Integer> supportPay;
    }

    /**
     * 预定规则
     */
    @Data
    public static class JinjiangBookRule {

        /**
         * 预付返现
         */
        private BigDecimal advanceBackCash;

        /**
         * 提前预订最大天数
         */
        private String bmaxDay;

        /**
         * 提前预订最小天数
         */
        private String bminDay;

        /**
         * 预订限制
         */
        private List<JinjiangBookConstraint> bookConstranit;

        /**
         * 入住范围结束日期
         */
        private String checkEndDateTime;

        /**
         * 最大入住天数
         */
        private Integer checkMaxDay;

        /**
         * 最小入住天数
         */
        private Integer checkMinDay;

        /**
         * 入住范围开始日期
         */
        private String checkStartDateTime;

        /**
         * 可入住的星期几
         */
        private String checkWeeks;

        /**
         * 预定范围结束日期
         */
        private String endDateTime;

        /**
         * 入住人数
         */
        private String maxAmount;

        /**
         * 最大连住天数
         */
        private Integer maxStayThrough;

        /**
         * 最小连住天数
         */
        private Integer minStayThrough;

        /**
         * 最大预订房间数
         */
        private Integer roomMaxAmount;

        /**
         * 最小预订房间数
         */
        private Integer roomMinAmount;

        /**
         * 到付返现
         */
        private BigDecimal spotBackCash;

        /**
         * 预定范围开始日期
         */
        private String startDateTime;

        /**
         * 可预定的星期几
         */
        private String weeks;
    }

    /**
     * 预订限制
     */
    @Data
    public static class JinjiangBookConstraint {

        /**
         * 预订检查类型
         */
        private Integer bookCheck;

        /**
         * 预订限制值
         */
        private String bookCheckV;
    }

    /**
     * 取消规则
     */
    @Data
    public static class JinjiangCancelRule {

        /**
         * 取消规则列表
         */
        private List<JinjiangCancelPenalty> cancelPenaltyList;

        /**
         * 新取消规则列表
         */
        private List<JinjiangNewCancelPenalty> newCancelPenaltyList;

        /**
         * 取消方式
         */
        private Integer supportCancel;
    }

    /**
     * 取消罚金
     */
    @Data
    public static class JinjiangCancelPenalty {

        /**
         * 取消类型
         */
        private String cancelType;

        /**
         * 结束时间
         */
        private String end;

        /**
         * 开始时间
         */
        private String start;

        /**
         * 罚金类型
         */
        private String type;

        /**
         * 罚款值
         */
        private String value;
    }

    /**
     * 新取消罚金
     */
    @Data
    public static class JinjiangNewCancelPenalty {

        /**
         * 取消类型
         */
        private String cancelType;

        /**
         * 结束时间
         */
        private Integer endHour;

        /**
         * 最大房间数范围
         */
        private Integer maxCount;

        /**
         * 最小房间数范围
         */
        private Integer minCount;

        /**
         * 开始时间
         */
        private Integer startHour;

        /**
         * 罚金类型
         */
        private String type;

        /**
         * 罚款值
         */
        private String value;
    }

    /**
     * 入住有礼详情
     */
    @Data
    public static class JinjiangPreferentInfo {

        /**
         * 其他权益
         */
        private String otherPreferentContent;

        /**
         * 权益是否叠加
         */
        private Integer overlay;

        /**
         * 权益列表
         */
        private List<Integer> preferentList;

        /**
         * 促销类型
         */
        private Integer salesType;
    }
}
