package org.xfy.hotelpricecomparison.dto.response;

import lombok.Data;
import java.util.List;

/**
 * 锦江酒店图片响应DTO
 */
@Data
public class JinjiangHotelImageResponse {
    
    /**
     * 酒店图片列表
     */
    private List<ImageData> imageDatas;
    
    /**
     * 图片数据
     */
    @Data
    public static class ImageData {
        
        /**
         * 图片说明
         */
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
        private Integer imageType;
        
        /**
         * 图片访问地址
         */
        private String imageUrl;
        
        /**
         * 是否主图
         * 1: 主图
         * 0: 非主图
         */
        private Integer master;
        
        /**
         * 房型编码
         */
        private String roomTypeCode;
        
        /**
         * 图片尺寸类型
         * 1: 小图120*120
         * 2: 640*480
         */
        private Integer sizeType;
        
        /**
         * 图片上传文件名称
         */
        private String uploadFileName;
    }
}
