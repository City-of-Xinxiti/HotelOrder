package org.xfy.hotelpricecomparison.common;

/**
 * 供应方API常量定义
 * 
 * @author system
 * @since 1.0.0
 */
public class JinjiangConstants {
    // ========== 酒店基本信息接口常量 ==========
    
    /**
     * 语言类型
     */
    public static class LanguageCode {
        public static final int CHINESE = 0;  // 中文
        public static final int ENGLISH = 1;  // 英文
    }

    /**
     * 经营类别
     */
    public static class BusinessType {
        public static final int SELF_OPERATED = 0;  // 自营店
        public static final int MANAGED = 1;       // 管理店
        public static final int FRANCHISED = 2;     // 加盟店
        public static final int STANDARD = 3;      // 标准店
        public static final int COOPERATIVE = 4;    // 合作店
    }

    /**
     * 酒店状态
     */
    public static class HotelStatus {
        public static final int DEVELOPMENT = -1;  // 开发
        public static final int PREPARING = 0;      // 筹建中
        public static final int OPEN = 1;          // 开业
        public static final int REBUILDING = 2;    // 开业后退筹建
        public static final int TERMINATED = 3;    // 开业后解约
        public static final int OFFLINE = 5;       // 下线整改
        public static final int SOLD = 6;          // 售出未下
    }

    /**
     * 酒店类别
     */
    public static class InnType {
        public static final int ECONOMY = 100;           // 经济型酒店
        public static final int BUSINESS = 101;          // 精品商务酒店
        public static final int RESORT = 102;            // 景区度假酒店
        public static final int THEME = 103;             // 主题特色酒店
        public static final int ETHNIC = 104;            // 民族风情酒店
    }

    /**
     * 星级类别
     */
    public static class StarType {
        public static final int ONE_STAR = 0;      // 一星
        public static final int TWO_STAR = 1;      // 二星
        public static final int THREE_STAR = 2;    // 三星
        public static final int FOUR_STAR = 3;     // 四星
        public static final int FIVE_STAR = 4;     // 五星
        public static final int NO_STAR = -1;      // 无星
    }

    /**
     * 酒店级别
     */
    public static class InnLevel {
        public static final int ECONOMY = 0;       // 经济型
        public static final int COMFORT = 1;       // 舒适型
        public static final int QUALITY = 2;       // 品质型
        public static final int HIGH_END = 3;      // 高档型
        public static final int LUXURY = 4;        // 豪华型
    }

    /**
     * 证件类型
     */
    public static class CertificateType {
        public static final int ID_CARD = 0;                    // 居民身份证
        public static final int TEMP_ID_CARD = 1;               // 临时居民身份证
        public static final int HOUSEHOLD_REGISTER = 2;          // 中华人民共和国居民户口簿
        public static final int PASSPORT = 3;                    // 护照
        public static final int SEAMAN_CERT = 4;                 // 海员证
        public static final int HK_MACAU_PASS = 5;               // 中华人民共和国往来港澳通行证
        public static final int HK_MACAU_OFFICIAL_PASS = 6;      // 因公往来香港澳门特别行政区通行证
    }

    // ========== 酒店房型接口常量 ==========

    /**
     * 床型
     */
    public static class BedType {
        public static final int KING_BED = 0;      // 大床
        public static final int TWIN_BED = 1;       // 双床
        public static final int SINGLE_BED = 2;     // 单床
        public static final int OTHER = 101;       // 其他床型
    }

    /**
     * 床宽
     */
    public static class BedWidth {
        public static final int WIDTH_1M = 0;       // 床宽1米
        public static final int WIDTH_1_1M = 1;     // 床宽1.1米
        public static final int WIDTH_1_2M = 2;     // 床宽1.2米
        public static final int WIDTH_1_3M = 3;     // 床宽1.3米
        public static final int WIDTH_1_35M = 4;    // 床宽1.35米
        public static final int WIDTH_1_4M = 5;     // 床宽1.4米
        public static final int WIDTH_1_5M = 6;     // 床宽1.5米
        public static final int WIDTH_1_6M = 7;     // 床宽1.6米
        public static final int WIDTH_1_65M = 8;    // 床宽1.65米
        public static final int WIDTH_1_8M = 9;     // 床宽1.8米
        public static final int WIDTH_2M = 10;      // 床宽2米
        public static final int WIDTH_2_2M = 11;    // 床宽2.2米
        public static final int WIDTH_2_3M = 12;   // 床宽2.3米
        public static final int WIDTH_2_4M = 13;   // 床宽2.4米
        public static final int WIDTH_2_6M = 14;   // 床宽2.6米
        public static final int WIDTH_2_7M = 15;    // 床宽2.7米
        public static final int WIDTH_0_9M = 16;    // 床宽0.9米
        public static final int OTHER = 101;        // 其他宽度
    }

    /**
     * 窗户类型
     */
    public static class WindowType {
        public static final int NO_WINDOW = 0;     // 无窗
        public static final int OUTER_WINDOW = 1;  // 外窗
        public static final int PARTIAL_NO_WINDOW = 2; // 部分无窗
        public static final int INNER_WINDOW = 3;  // 内窗
        public static final int PARTIAL_INNER_WINDOW = 4; // 部分内窗
    }

    // ========== 酒店房态接口常量 ==========

    /**
     * 报价类型
     */
    public static class PriceType {
        public static final int BASE_AGREEMENT = 1;  // 基础协议价
        public static final int PREMIUM = 2;          // 追价(基础协议价+普卡活动价)
    }

    /**
     * 商品类型
     */
    public static class ProductType {
        public static final int STORE_AGREEMENT = 0;        // 本店协议价
        public static final int BATCH_BRAND_AGREEMENT = 1;  // 批量分店品牌协议价
        public static final int COMBINED_BRAND_AGREEMENT = 2; // 组合品牌协议价
        public static final int PLATFORM_AGREEMENT = 3;     // 平台协议价
        public static final int ENTERPRISE_ACTIVITY = 4;     // 企业活动价
        public static final int EXCLUSIVE_ACTIVITY = 5;     // 专属活动价
    }

    /**
     * 协议类型
     */
    public static class ProtocolType {
        public static final int STORE_PROTOCOL = 0;         // 分店协议
        public static final int BRAND_BATCH_PROTOCOL = 1;   // 品牌批量分店协议
        public static final int BRAND_COMBINED_PROTOCOL = 2; // 品牌组合协议
        public static final int SELF_SALES_STORE = 3;       // 自营销售分店协议
        public static final int SELF_SALES_BRAND_BATCH = 4; // 自营销售品牌批量分店协议
        public static final int SELF_SALES_BRAND_COMBINED = 5; // 自营销售品牌组合协议
    }

    /**
     * 取消方式
     */
    public static class SupportCancel {
        public static final int NO_CANCEL = 0;     // 不可取消
        public static final int FREE_CANCEL = 1;    // 免费取消
        public static final int TIME_LIMITED_CANCEL = 2; // 限时取消
    }

    /**
     * 取消类型
     */
    public static class CancelType {
        public static final int FREE = 0;           // 免费取消
        public static final int CHARGED = 1;        // 有偿取消
    }

    /**
     * 罚金类型
     */
    public static class PenaltyType {
        public static final int ROOM_NIGHT = 0;     // 房晚
        public static final int PERCENTAGE = 1;     // 百分数
        public static final int FIXED_AMOUNT = 2;   // 固定罚金
    }

    // ========== 下单预订接口常量 ==========

    /**
     * 预订人证件类型
     */
    public static class BookIdCardType {
        public static final int ID_CARD = 10;      // 身份证
        public static final int TAIWAN_PASS = 11;  // 台湾居民来往大陆通行证
        public static final int HK_MACAU_PASS = 12; // 港澳来往大陆通行证
        public static final int FOREIGN_PASSPORT = 13; // 外籍护照
    }

    // ========== 查看订单信息接口常量 ==========

    /**
     * 订单状态
     */
    public static class OrderState {
        public static final int PENDING_CONFIRM = 0;  // 待确认
        public static final int BOOKED = 1;            // 预订成功
        public static final int CANCELLED = 2;        // 已取消
        public static final int NO_SHOW = 3;          // 预订未到
        public static final int CHECKED_IN = 4;       // 已入住
        public static final int COMPLETED = 5;        // 已完成
        public static final int CONFIRM_FAILED = 6;   // 确认失败
    }

    /**
     * 支付方式
     */
    public static class PayType {
        public static final int PAY_ON_ARRIVAL = 0;    // 到付
        public static final int PREPAID = 1;           // 线上预付
    }

    /**
     * 支付状态
     */
    public static class PayState {
        public static final int UNPAID = 0;            // 未付款
        public static final int PAID = 1;              // 已付款
        public static final int REFUNDING = 2;         // 退款处理中
        public static final int REFUNDED = 3;          // 已退款
    }

    /**
     * 担保类型
     */
    public static class AssureType {
        public static final int NO_GUARANTEE = 0;      // 无担保
        public static final int FULL_GUARANTEE = 2;    // 全程担保
    }

    /**
     * 发票类型
     */
    public static class InvoiceType {
        public static final int NO_INVOICE = 0;        // 无
        public static final int REGULAR_INVOICE = 1;   // 普通发票
        public static final int VAT_REGULAR = 2;       // 增值税普通发票
        public static final int VAT_SPECIAL = 3;       // 增值税专用发票
        public static final int VAT_ELECTRONIC = 4;    // 电子增值税普通发票
    }

    // ========== 早餐类型常量 ==========

    /**
     * 早餐类型
     */
    public static class BreakfastType {
        public static final int NONE = 0;                    // 无
        public static final int SINGLE = 1;                  // 单早
        public static final int DOUBLE = 2;                  // 双早
    }

    // ========== 新增常量 ==========

    /**
     * 供应商编码
     */
    public static class SupplierCode {
        public static final String JINJIANG = "JINJIANG";
        public static final String QUNAR = "QUNAR";
    }

    /**
     * 品牌状态
     */
    public static class BrandStatus {
        public static final int ENABLED = 1;
        public static final int DISABLED = 0;
    }

    /**
     * 品牌类型
     */
    public static class BrandType {
        public static final int ECONOMY = 1;      // 简约舒适
        public static final int BOUTIQUE = 2;     // 精品优选
        public static final int LUXURY = 3;       // 豪华高端
    }

    /**
     * 默认值
     */
    public static class DefaultValues {
        public static final int MISSING_COUNT = 0;
        public static final int LANGUAGE_CODE = LanguageCode.CHINESE;
        public static final int BRAND_STATUS = BrandStatus.ENABLED;
    }

    /**
     * API相关常量
     */
    public static class ApiHeaders {
        public static final String APP_ID = "app-id";
        public static final String TIMESTAMP = "timestamp";
        public static final String SIGN = "sign";
    }

    /**
     * API请求参数键
     */
    public static class ApiRequestKeys {
        public static final String PARAM = "param";
    }
}
