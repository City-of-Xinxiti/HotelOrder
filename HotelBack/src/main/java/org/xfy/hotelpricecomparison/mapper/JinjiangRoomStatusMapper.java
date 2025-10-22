package org.xfy.hotelpricecomparison.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xfy.hotelpricecomparison.entity.JinjiangRoomStatus;

import java.util.List;

/**
 * 锦江房态Mapper接口
 * 
 * @author system
 * @since 1.0.0
 */
@Mapper
public interface JinjiangRoomStatusMapper extends BaseMapper<JinjiangRoomStatus> {

    /**
     * 根据酒店ID查询房态列表
     */
    List<JinjiangRoomStatus> selectByInnId(@Param("innId") String innId);

    /**
     * 根据房型编码查询房态列表
     */
    List<JinjiangRoomStatus> selectByRoomTypeCode(@Param("roomTypeCode") String roomTypeCode);

    /**
     * 根据商品编码查询房态列表
     */
    List<JinjiangRoomStatus> selectByProductCode(@Param("productCode") String productCode);

    /**
     * 根据营业日期查询房态列表
     */
    List<JinjiangRoomStatus> selectByEndOfDay(@Param("endOfDay") Long endOfDay);

    /**
     * 根据酒店ID和营业日期查询房态列表
     */
    List<JinjiangRoomStatus> selectByInnIdAndDate(@Param("innId") String innId, @Param("endOfDay") Long endOfDay);

    /**
     * 根据酒店ID、营业日期和天数查询房态列表
     */
    List<JinjiangRoomStatus> selectByInnIdAndDate(@Param("innId") String innId, @Param("endOfDay") Long endOfDay, @Param("days") Integer days);

    /**
     * 根据酒店ID和房型编码查询房态列表
     */
    List<JinjiangRoomStatus> selectByInnIdAndRoomType(@Param("innId") String innId, @Param("roomTypeCode") String roomTypeCode);

    /**
     * 批量插入房态
     */
    int batchInsert(@Param("roomStatuses") List<JinjiangRoomStatus> roomStatuses);

    /**
     * 批量更新房态
     */
    int batchUpdate(@Param("roomStatuses") List<JinjiangRoomStatus> roomStatuses);

    /**
     * 根据酒店ID删除房态
     */
    int deleteByInnId(@Param("innId") String innId);

    /**
     * 根据酒店ID、营业日期和天数查询房态列表
     */
    List<JinjiangRoomStatus> selectByInnIdAndDateRange(@Param("innId") String innId, @Param("endOfDay") Long endOfDay, @Param("days") Integer days);
}
