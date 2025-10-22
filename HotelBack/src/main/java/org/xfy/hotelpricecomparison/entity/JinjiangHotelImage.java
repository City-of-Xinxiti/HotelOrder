package org.xfy.hotelpricecomparison.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 锦江酒店相册图片表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jinjiang_hotel_images")
public class JinjiangHotelImage {
    
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
     * 图片说明
     */
    @TableField("image_des")
    private String imageDes;
    
    /**
     * 图片类型
     * 1: 酒店图片
     * 2: 客房图片
     * 3: 酒店外观
     * 4: 酒店大堂
     * 5: 酒店娱乐设施
     * 6: 酒店餐饮设施
     * 7: 酒店服务设施
     * 8: 酒店休闲设施
     * 9: 其他
     * 10: 公共区域
     * 11: 周边景点
     */
    @TableField("image_type")
    private Integer imageType;
    
    /**
     * 图片访问地址
     */
    @TableField("image_url")
    private String imageUrl;
    
    /**
     * 是否主图
     * 1: 主图
     * 0: 非主图
     */
    @TableField("master")
    private Integer master;
    
    /**
     * 房型编码
     */
    @TableField("room_type_code")
    private String roomTypeCode;
    
    /**
     * 图片尺寸类型
     * 1: 小图120*120
     * 2: 640*480
     */
    @TableField("size_type")
    private Integer sizeType;
    
    /**
     * 图片上传文件名称
     */
    @TableField("upload_file_name")
    private String uploadFileName;
    
    /**
     * 语言类型
     * 0: 中文
     * 1: 英文
     */
    @TableField("language_code")
    private Integer languageCode;
    
    /**
     * 是否有效
     * 1: 有效
     * 0: 无效
     */
    @TableField("valid")
    private Integer valid;
    
    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}