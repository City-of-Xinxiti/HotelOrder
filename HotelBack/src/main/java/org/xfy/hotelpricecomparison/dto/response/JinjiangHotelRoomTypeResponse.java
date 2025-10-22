package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 锦江酒店房型响应DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangHotelRoomTypeResponse {

    /**
     * 酒店房态列表
     */
    private List<JinjiangRoomTypeData> roomTypeData;

    /**
     * 房型数据
     */
    @Data
    public static class JinjiangRoomTypeData {

        /**
         * 是否允许加床（0：否 1：是）
         */
        private Integer addBed;

        /**
         * 面积
         */
        private String area;

        /**
         * 床数
         */
        private Integer bedCount;

        /**
         * 床型
         */
        private Integer bedType;

        /**
         * 床宽
         */
        private String bedWidth;

        /**
         * 是否含有加床费(1 是 0 否)
         */
        private Integer extraBedFee;

        /**
         * 房间设施列表
         */
        private List<RoomFacility> facList;

        /**
         * 楼层
         */
        private String floor;

        /**
         * 最多入住人数
         */
        private Integer maxCheckIn;

        /**
         * 最大房间数量
         */
        private String maxRoom;

        /**
         * 房型编号
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
         * 房型唯一编号
         */
        private String sCode;

        /**
         * 特色服务列表
         */
        private List<RoomSpecial> spList;

        /**
         * 是否有窗
         */
        private Integer window;
    }

    /**
     * 房型设施
     */
    @Data
    public static class RoomFacility {
        private String facCode;
        private String facName;
        private Integer valid;
    }

    /**
     * 房型特色服务
     */
    @Data
    public static class RoomSpecial {
        private String spCode;
        private String spName;
        private Integer valid;
    }
}
