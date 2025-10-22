package org.xfy.hotelpricecomparison.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xfy.hotelpricecomparison.entity.JinjiangHotel;

import java.util.List;

/**
 * 锦江酒店Mapper接口
 * 
 * @author system
 * @since 1.0.0
 */
@Mapper
public interface JinjiangHotelMapper extends BaseMapper<JinjiangHotel> {

    /**
     * 根据酒店ID查询酒店
     */
    JinjiangHotel selectByInnId(@Param("innId") String innId);

    /**
     * 根据城市编码查询酒店列表
     */
    List<JinjiangHotel> selectByCityCode(@Param("cityCode") String cityCode);

    /**
     * 根据品牌编码查询酒店列表
     */
    List<JinjiangHotel> selectByBrandCode(@Param("brandCode") String brandCode);

    /**
     * 根据酒店名称模糊查询
     */
    List<JinjiangHotel> selectByNameLike(@Param("hotelName") String hotelName);

    /**
     * 根据状态查询酒店列表
     */
    List<JinjiangHotel> selectByStatus(@Param("status") Integer status);

    /**
     * 根据语言类型查询酒店列表
     */
    List<JinjiangHotel> selectByLanguageCode(@Param("languageCode") Integer languageCode);

    /**
     * 批量插入酒店
     */
    int batchInsert(@Param("hotels") List<JinjiangHotel> hotels);

    /**
     * 批量更新酒店
     */
    int batchUpdate(@Param("hotels") List<JinjiangHotel> hotels);

    /**
     * 查询所有酒店
     */
    List<JinjiangHotel> selectAllHotels();
}
