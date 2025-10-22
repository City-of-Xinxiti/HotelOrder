package org.xfy.hotelpricecomparison.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xfy.hotelpricecomparison.entity.Order;

/**
 * 订单Mapper接口
 * 
 * @author system
 * @since 1.0.0
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
