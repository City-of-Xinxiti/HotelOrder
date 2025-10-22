package org.xfy.hotelpricecomparison.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 锦江房型实体类
 * 对应锦江API房型接口
 * 
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jinjiang_room_types")
public class JinjiangRoomType {

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
     * 房型唯一编号
     */
    @TableField("s_code")
    private String sCode;

    /**
     * 房型编号(兼容第三方)
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
     * 是否允许加床（0：否 1：是）
     */
    @TableField("add_bed")
    private Integer addBed;

    /**
     * 面积
     */
    @TableField("area")
    private String area;

    /**
     * 床数
     */
    @TableField("bed_count")
    private Integer bedCount;

    /**
     * 床型（1双床 2 单床 0 大床 101 其他床型）
     */
    @TableField("bed_type")
    private Integer bedType;

    /**
     * 床宽
     */
    @TableField("bed_width")
    private String bedWidth;

    /**
     * 是否含有加床费(1 是 0 否)
     */
    @TableField("extra_bed_fee")
    private Integer extraBedFee;

    /**
     * 楼层
     */
    @TableField("floor")
    private String floor;

    /**
     * 最多入住人数
     */
    @TableField("max_check_in")
    private Integer maxCheckIn;

    /**
     * 最大房间数量
     */
    @TableField("max_room")
    private String maxRoom;

    /**
     * 是否有窗
     */
    @TableField("window")
    private Integer window;

    /**
     * 语言类型(0 中文 1 英文)
     */
    @TableField("language_code")
    private Integer languageCode;

    /**
     * 是否有效(1 有效 0 无效)
     */
    @TableField("valid")
    private Integer valid;

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
