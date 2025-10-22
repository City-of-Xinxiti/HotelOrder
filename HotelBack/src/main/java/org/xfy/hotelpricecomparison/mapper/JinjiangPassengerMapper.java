package org.xfy.hotelpricecomparison.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xfy.hotelpricecomparison.entity.JinjiangPassenger;

import java.util.List;

/**
 * 锦江入住人Mapper接口
 * 
 * @author system
 * @since 1.0.0
 */
@Mapper
public interface JinjiangPassengerMapper extends BaseMapper<JinjiangPassenger> {

    /**
     * 根据订单ID查询入住人列表
     */
    List<JinjiangPassenger> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据入住人姓名查询入住人列表
     */
    List<JinjiangPassenger> selectByGuestName(@Param("guestName") String guestName);

    /**
     * 根据入住人手机查询入住人列表
     */
    List<JinjiangPassenger> selectByGuestMobile(@Param("guestMobile") String guestMobile);

    /**
     * 根据入住人证件号查询入住人列表
     */
    List<JinjiangPassenger> selectByGuestIdCard(@Param("guestIdCard") String guestIdCard);

    /**
     * 批量插入入住人
     */
    int batchInsert(@Param("passengers") List<JinjiangPassenger> passengers);

    /**
     * 根据订单ID删除入住人
     */
    int deleteByOrderId(@Param("orderId") Long orderId);
}
