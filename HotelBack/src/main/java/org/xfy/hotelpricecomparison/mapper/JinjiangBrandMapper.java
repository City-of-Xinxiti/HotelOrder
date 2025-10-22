package org.xfy.hotelpricecomparison.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xfy.hotelpricecomparison.entity.JinjiangBrand;

import java.util.List;

/**
 * 锦江品牌Mapper接口
 * 
 * @author system
 * @since 1.0.0
 */
@Mapper
public interface JinjiangBrandMapper extends BaseMapper<JinjiangBrand> {

    /**
     * 根据品牌编码查询品牌
     */
    JinjiangBrand selectByBrandCode(@Param("brandCode") String brandCode);

    /**
     * 根据品牌类型查询品牌列表
     */
    List<JinjiangBrand> selectByBrandType(@Param("brandType") Integer brandType);

    /**
     * 根据语言类型查询品牌列表
     */
    List<JinjiangBrand> selectByLanguageCode(@Param("languageCode") Integer languageCode);

    /**
     * 根据品牌名称模糊查询
     */
    List<JinjiangBrand> selectByNameLike(@Param("brandName") String brandName);

    /**
     * 批量插入品牌
     */
    int batchInsert(@Param("brands") List<JinjiangBrand> brands);

    /**
     * 批量更新品牌
     */
    int batchUpdate(@Param("brands") List<JinjiangBrand> brands);

    /**
     * 查询所有品牌
     */
    List<JinjiangBrand> selectAllBrands();
}
