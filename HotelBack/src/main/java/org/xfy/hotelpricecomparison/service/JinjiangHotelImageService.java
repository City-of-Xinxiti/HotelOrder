package org.xfy.hotelpricecomparison.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xfy.hotelpricecomparison.entity.JinjiangHotelImage;

import java.util.List;

/**
 * 锦江酒店图片服务接口
 */
public interface JinjiangHotelImageService extends IService<JinjiangHotelImage> {
    
    /**
     * 根据酒店ID和图片类型查询图片
     * @param innId 酒店ID
     * @param imageType 图片类型
     * @param languageCode 语言类型
     * @return 图片列表
     */
    List<JinjiangHotelImage> getImagesByInnIdAndType(String innId, Integer imageType, Integer languageCode);
    
    /**
     * 根据酒店ID和房型编码查询图片
     * @param innId 酒店ID
     * @param roomTypeCode 房型编码
     * @param languageCode 语言类型
     * @return 图片列表
     */
    List<JinjiangHotelImage> getImagesByInnIdAndRoomType(String innId, String roomTypeCode, Integer languageCode);
    
    /**
     * 根据酒店ID查询主图
     * @param innId 酒店ID
     * @param languageCode 语言类型
     * @return 主图列表
     */
    List<JinjiangHotelImage> getMasterImagesByInnId(String innId, Integer languageCode);
    
    /**
     * 同步酒店图片数据
     * @param innId 酒店ID
     * @param languageCode 语言类型
     * @return 同步结果
     */
    boolean syncHotelImages(String innId, Integer languageCode);
}
