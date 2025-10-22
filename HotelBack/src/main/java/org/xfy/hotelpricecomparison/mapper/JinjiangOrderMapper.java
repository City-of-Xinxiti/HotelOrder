package org.xfy.hotelpricecomparison.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xfy.hotelpricecomparison.entity.JinjiangOrder;

import java.util.List;

/**
 * 锦江订单Mapper接口
 * 
 * @author system
 * @since 1.0.0
 */
@Mapper
public interface JinjiangOrderMapper extends BaseMapper<JinjiangOrder> {

    /**
     * 根据锦江订单号查询订单
     */
    JinjiangOrder selectByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据外部订单号查询订单
     */
    JinjiangOrder selectByExternalId(@Param("externalId") String externalId);

    /**
     * 根据酒店ID查询订单列表
     */
    List<JinjiangOrder> selectByInnId(@Param("innId") String innId);

    /**
     * 根据预订人姓名查询订单列表
     */
    List<JinjiangOrder> selectByBookName(@Param("bookName") String bookName);

    /**
     * 根据预订人手机查询订单列表
     */
    List<JinjiangOrder> selectByBookMobile(@Param("bookMobile") String bookMobile);

    /**
     * 根据订单状态查询订单列表
     */
    List<JinjiangOrder> selectByOrderState(@Param("orderState") Integer orderState);

    /**
     * 根据支付方式查询订单列表
     */
    List<JinjiangOrder> selectByPayType(@Param("payType") Integer payType);

    /**
     * 根据入住日期范围查询订单列表
     */
    List<JinjiangOrder> selectByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
