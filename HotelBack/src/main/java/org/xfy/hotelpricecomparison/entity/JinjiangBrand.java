package org.xfy.hotelpricecomparison.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 锦江品牌实体类
 * 对应锦江API品牌列表接口
 * 
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jinjiang_brands")
public class JinjiangBrand {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 品牌唯一编号
     */
    @TableField("brand_code")
    private String brandCode;

    /**
     * 品牌名字
     */
    @TableField("brand_name")
    private String brandName;

    /**
     * 品牌类型(1 简约舒适 2 精品优选 3 豪华高端)
     */
    @TableField("brand_type")
    private Integer brandType;

    /**
     * 品牌描述
     */
    @TableField("description")
    private String description;

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

    /**
     * 获取品牌类型描述
     */
    public String getBrandTypeDesc() {
        if (brandType == null) return "未知";
        switch (brandType) {
            case 1: return "简约舒适";
            case 2: return "精品优选";
            case 3: return "豪华高端";
            default: return "未知";
        }
    }
}
