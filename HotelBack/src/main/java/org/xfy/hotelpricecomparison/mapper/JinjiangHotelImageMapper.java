package org.xfy.hotelpricecomparison.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xfy.hotelpricecomparison.entity.JinjiangHotelImage;

import java.util.List;

/**
 * 锦江酒店图片Mapper接口
 */
@Mapper
public interface JinjiangHotelImageMapper extends BaseMapper<JinjiangHotelImage> {
    
    /**
     * 根据酒店ID和图片类型查询图片
     * @param innId 酒店ID
     * @param imageType 图片类型
     * @param languageCode 语言类型
     * @return 图片列表
     */
    List<JinjiangHotelImage> selectByInnIdAndImageType(
        @Param("innId") String innId, 
        @Param("imageType") Integer imageType,
        @Param("languageCode") Integer languageCode
    );
    
    /**
     * 根据酒店ID和房型编码查询图片
     * @param innId 酒店ID
     * @param roomTypeCode 房型编码
     * @param languageCode 语言类型
     * @return 图片列表
     */
    List<JinjiangHotelImage> selectByInnIdAndRoomType(
        @Param("innId") String innId, 
        @Param("roomTypeCode") String roomTypeCode,
        @Param("languageCode") Integer languageCode
    );
    
    /**
     * 根据酒店ID查询主图
     * @param innId 酒店ID
     * @param languageCode 语言类型
     * @return 主图列表
     */
    List<JinjiangHotelImage> selectMasterImagesByInnId(
        @Param("innId") String innId,
        @Param("languageCode") Integer languageCode
    );
}