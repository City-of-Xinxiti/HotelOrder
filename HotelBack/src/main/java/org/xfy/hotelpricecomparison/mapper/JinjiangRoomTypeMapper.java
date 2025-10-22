package org.xfy.hotelpricecomparison.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xfy.hotelpricecomparison.entity.JinjiangRoomType;

import java.util.List;

/**
 * 锦江房型Mapper接口
 * 
 * @author system
 * @since 1.0.0
 */
@Mapper
public interface JinjiangRoomTypeMapper extends BaseMapper<JinjiangRoomType> {

    /**
     * 根据酒店ID查询房型列表
     */
    List<JinjiangRoomType> selectByInnId(@Param("innId") String innId);

    /**
     * 根据房型编码查询房型
     */
    JinjiangRoomType selectByRoomTypeCode(@Param("roomTypeCode") String roomTypeCode);

    /**
     * 根据房型唯一编号查询房型
     */
    JinjiangRoomType selectBySCode(@Param("sCode") String sCode);

    /**
     * 根据房型名称模糊查询
     */
    List<JinjiangRoomType> selectByNameLike(@Param("roomTypeName") String roomTypeName);

    /**
     * 根据状态查询房型列表
     */
    List<JinjiangRoomType> selectByValid(@Param("valid") Integer valid);

    /**
     * 根据语言类型查询房型列表
     */
    List<JinjiangRoomType> selectByLanguageCode(@Param("languageCode") Integer languageCode);

    /**
     * 批量插入房型
     */
    int batchInsert(@Param("roomTypes") List<JinjiangRoomType> roomTypes);

    /**
     * 批量更新房型
     */
    int batchUpdate(@Param("roomTypes") List<JinjiangRoomType> roomTypes);
}
