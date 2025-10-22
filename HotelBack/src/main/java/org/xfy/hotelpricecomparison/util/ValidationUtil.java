package org.xfy.hotelpricecomparison.util;

import lombok.extern.slf4j.Slf4j;
import org.xfy.hotelpricecomparison.dto.response.JinjiangBrandResponse;
import org.xfy.hotelpricecomparison.entity.JinjiangBrand;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据验证工具类
 * 提供数据验证和清理功能
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
public class ValidationUtil {

    /**
     * 品牌编码正则表达式（字母数字组合，3-20位）
     */
    private static final Pattern BRAND_CODE_PATTERN = Pattern.compile("^[A-Za-z0-9]{3,20}$");

    /**
     * 品牌名称最大长度
     */
    private static final int BRAND_NAME_MAX_LENGTH = 100;

    /**
     * 描述最大长度
     */
    private static final int DESCRIPTION_MAX_LENGTH = 500;

    /**
     * 验证品牌数据
     * 
     * @param supplierBrand 供应商品牌数据
     * @return 验证结果
     */
    public static boolean validateSupplierBrand(JinjiangBrandResponse.JinjiangBrand JinjiangBrand) {
        if (JinjiangBrand == null) {
            log.warn("品牌数据为空");
            return false;
        }

        // 验证品牌编码
        if (!isValidBrandCode(JinjiangBrand.getBrandCode())) {
            log.warn("品牌编码无效: {}", JinjiangBrand.getBrandCode());
            return false;
        }

        // 验证品牌名称
        if (!isValidBrandName(JinjiangBrand.getBrandName())) {
            log.warn("品牌名称无效: {}", JinjiangBrand.getBrandName());
            return false;
        }

        // 验证品牌类型
        if (!isValidBrandType(JinjiangBrand.getBrandType())) {
            log.warn("品牌类型无效: {}", JinjiangBrand.getBrandType());
            return false;
        }

        return true;
    }

    /**
     * 验证品牌编码
     */
    private static boolean isValidBrandCode(String brandCode) {
        return brandCode != null && 
               !brandCode.trim().isEmpty() && 
               BRAND_CODE_PATTERN.matcher(brandCode).matches();
    }

    /**
     * 验证品牌名称
     */
    private static boolean isValidBrandName(String brandName) {
        return brandName != null && 
               !brandName.trim().isEmpty() && 
               brandName.length() <= BRAND_NAME_MAX_LENGTH;
    }

    /**
     * 验证品牌类型
     */
    private static boolean isValidBrandType(Integer brandType) {
        return brandType != null && 
               brandType >= 1 && 
               brandType <= 3;
    }

    /**
     * 清理和验证品牌数据列表
     * 
     * @param supplierBrands 供应商品牌列表
     * @return 验证通过的品牌列表
     */
    public static List<JinjiangBrandResponse.JinjiangBrand> validateAndCleanBrandList(
            List<JinjiangBrandResponse.JinjiangBrand> JinjiangBrandBrands) {
        
        if (JinjiangBrandBrands == null || JinjiangBrandBrands.isEmpty()) {
            log.warn("品牌列表为空");
            return new ArrayList<>();
        }

    List<JinjiangBrandResponse.JinjiangBrand> validBrands = new ArrayList<>();
        int invalidCount = 0;

        for (JinjiangBrandResponse.JinjiangBrand brand : JinjiangBrandBrands) {
            if (validateSupplierBrand(brand)) {
                validBrands.add(brand);
            } else {
                invalidCount++;
            }
        }

        if (invalidCount > 0) {
            log.warn("发现 {} 个无效的品牌数据", invalidCount);
        }

        log.info("品牌数据验证完成，有效数据: {}, 无效数据: {}", validBrands.size(), invalidCount);
        return validBrands;
    }

    /**
     * 清理品牌实体数据
     * 
     * @param brand 品牌实体
     */
    public static void cleanBrandEntity(JinjiangBrand brand) {
        if (brand == null) {
            return;
        }

        // 清理品牌编码
        if (brand.getBrandCode() != null) {
            brand.setBrandCode(brand.getBrandCode().trim().toUpperCase());
        }

        // 清理品牌名称
        if (brand.getBrandName() != null) {
            brand.setBrandName(brand.getBrandName().trim());
        }

        // 清理描述
        if (brand.getDescription() != null) {
            String description = brand.getDescription().trim();
            if (description.length() > DESCRIPTION_MAX_LENGTH) {
                description = description.substring(0, DESCRIPTION_MAX_LENGTH);
            }
            brand.setDescription(description);
        }
    }

    /**
     * 验证酒店ID信息
     * 
     * @param hotelInfo 酒店ID信息
     * @return 验证结果
     */
    public static boolean validateHotelIdInfo(org.xfy.hotelpricecomparison.dto.response.JinjiangHotelIdsResponse.JinjiangHotelInfo hotelInfo) {
        if (hotelInfo == null) {
            log.warn("酒店ID信息为空");
            return false;
        }

        // 验证酒店ID
        if (!isValidHotelId(hotelInfo.getInnId())) {
            log.warn("酒店ID无效: {}", hotelInfo.getInnId());
            return false;
        }

        // 验证品牌编码（可选）
        if (hotelInfo.getBrandCode() != null && !isValidBrandCode(hotelInfo.getBrandCode())) {
            log.warn("品牌编码无效: {}", hotelInfo.getBrandCode());
            return false;
        }

        return true;
    }

    /**
     * 验证酒店ID
     */
    private static boolean isValidHotelId(String hotelId) {
        return hotelId != null && 
               !hotelId.trim().isEmpty() && 
               hotelId.length() >= 2 && 
               hotelId.length() <= 50;
    }

    /**
     * 清理和验证酒店ID列表
     * 
     * @param hotelInfoList 酒店ID信息列表
     * @return 验证通过且去重后的酒店ID列表
     */
    public static java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelIdsResponse.JinjiangHotelInfo> validateAndCleanHotelIdList(
            java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelIdsResponse.JinjiangHotelInfo> hotelInfoList) {
        
        if (hotelInfoList == null || hotelInfoList.isEmpty()) {
            log.warn("酒店ID列表为空");
            return new java.util.ArrayList<>();
        }

        java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelIdsResponse.JinjiangHotelInfo> validHotels = new java.util.ArrayList<>();
        java.util.Set<String> seenIds = new java.util.HashSet<>();
        int invalidCount = 0;
        int duplicateCount = 0;

        for (org.xfy.hotelpricecomparison.dto.response.JinjiangHotelIdsResponse.JinjiangHotelInfo hotelInfo : hotelInfoList) {
            if (validateHotelIdInfo(hotelInfo)) {
                // 去重
                if (!seenIds.contains(hotelInfo.getInnId())) {
                    seenIds.add(hotelInfo.getInnId());
                    validHotels.add(hotelInfo);
                } else {
                    duplicateCount++;
                    log.debug("发现重复的酒店ID: {}", hotelInfo.getInnId());
                }
            } else {
                invalidCount++;
            }
        }

        if (invalidCount > 0 || duplicateCount > 0) {
            log.warn("酒店ID数据处理完成，有效: {}, 无效: {}, 重复: {}", 
                validHotels.size(), invalidCount, duplicateCount);
        }

        log.info("酒店ID列表验证完成，有效数据: {}, 无效数据: {}, 重复数据: {}", 
            validHotels.size(), invalidCount, duplicateCount);
        return validHotels;
    }

    /**
     * 验证酒店信息
     * 
     * @param hotelInfo 酒店信息
     * @return 验证结果
     */
    public static boolean validateHotelInfo(org.xfy.hotelpricecomparison.dto.response.JinjiangHotelInfoResponse hotelInfo) {
        if (hotelInfo == null) {
            log.warn("酒店信息为空");
            return false;
        }

        // 验证酒店ID
        if (!isValidHotelId(hotelInfo.getInnId())) {
            log.warn("酒店ID无效: {}", hotelInfo.getInnId());
            return false;
        }

        // 验证酒店名称
        if (!isValidHotelName(hotelInfo.getInnName())) {
            log.warn("酒店名称无效: {}", hotelInfo.getInnName());
            return false;
        }

        // 验证品牌编码
        if (hotelInfo.getBrandCode() != null && !isValidBrandCode(hotelInfo.getBrandCode())) {
            log.warn("品牌编码无效: {}", hotelInfo.getBrandCode());
            return false;
        }

        return true;
    }

    /**
     * 验证酒店名称
     */
    private static boolean isValidHotelName(String hotelName) {
        return hotelName != null && 
               !hotelName.trim().isEmpty() && 
               hotelName.length() >= 2 && 
               hotelName.length() <= 200;
    }

    /**
     * 清理和验证酒店信息列表
     * 
     * @param hotelInfoList 酒店信息列表
     * @return 验证通过且去重后的酒店信息列表
     */
    public static java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelInfoResponse> validateAndCleanHotelInfoList(
            java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelInfoResponse> hotelInfoList) {
        
        if (hotelInfoList == null || hotelInfoList.isEmpty()) {
            log.warn("酒店信息列表为空");
            return new java.util.ArrayList<>();
        }

        java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelInfoResponse> validHotels = new java.util.ArrayList<>();
        java.util.Set<String> seenIds = new java.util.HashSet<>();
        int invalidCount = 0;
        int duplicateCount = 0;

        for (org.xfy.hotelpricecomparison.dto.response.JinjiangHotelInfoResponse hotelInfo : hotelInfoList) {
            if (validateHotelInfo(hotelInfo)) {
                // 去重
                if (!seenIds.contains(hotelInfo.getInnId())) {
                    seenIds.add(hotelInfo.getInnId());
                    validHotels.add(hotelInfo);
                } else {
                    duplicateCount++;
                    log.debug("发现重复的酒店ID: {}", hotelInfo.getInnId());
                }
            } else {
                invalidCount++;
            }
        }

        if (invalidCount > 0 || duplicateCount > 0) {
            log.warn("酒店信息数据处理完成，有效: {}, 无效: {}, 重复: {}", 
                validHotels.size(), invalidCount, duplicateCount);
        }

        log.info("酒店信息列表验证完成，有效数据: {}, 无效数据: {}, 重复数据: {}", 
            validHotels.size(), invalidCount, duplicateCount);
        return validHotels;
    }


    /**
     * 验证房型数据
     * 
     * @param roomTypeData 房型数据
     * @return 验证结果
     */
    public static boolean validateRoomTypeData(org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomTypeResponse.JinjiangRoomTypeData roomTypeData) {
        if (roomTypeData == null) {
            log.warn("房型数据为空");
            return false;
        }

        // 验证房型编码
        if (!isValidRoomTypeCode(roomTypeData.getRoomTypeCode())) {
            log.warn("房型编码无效: {}", roomTypeData.getRoomTypeCode());
            return false;
        }

        // 验证房型名称
        if (!isValidRoomTypeName(roomTypeData.getRoomTypeName())) {
            log.warn("房型名称无效: {}", roomTypeData.getRoomTypeName());
            return false;
        }

        // 验证最大入住人数
        if (roomTypeData.getMaxCheckIn() != null && roomTypeData.getMaxCheckIn() <= 0) {
            log.warn("最大入住人数无效: {}", roomTypeData.getMaxCheckIn());
            return false;
        }

        return true;
    }

    /**
     * 验证房型编码
     */
    private static boolean isValidRoomTypeCode(String roomTypeCode) {
        return roomTypeCode != null && 
               !roomTypeCode.trim().isEmpty() && 
               roomTypeCode.length() >= 3 && 
               roomTypeCode.length() <= 20;
    }

    /**
     * 验证房型名称
     */
    private static boolean isValidRoomTypeName(String roomTypeName) {
        return roomTypeName != null && 
               !roomTypeName.trim().isEmpty() && 
               roomTypeName.length() >= 2 && 
               roomTypeName.length() <= 100;
    }

    /**
     * 清理和验证房型数据列表
     * 
     * @param roomTypeDataList 房型数据列表
     * @return 验证通过且去重后的房型数据列表
     */
    public static java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomTypeResponse.JinjiangRoomTypeData> validateAndCleanRoomTypeDataList(
            java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomTypeResponse.JinjiangRoomTypeData> roomTypeDataList) {
        
        if (roomTypeDataList == null || roomTypeDataList.isEmpty()) {
            log.warn("房型数据列表为空");
            return new java.util.ArrayList<>();
        }

        java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomTypeResponse.JinjiangRoomTypeData> validRoomTypes = new java.util.ArrayList<>();
        java.util.Set<String> seenCodes = new java.util.HashSet<>();
        int invalidCount = 0;
        int duplicateCount = 0;

        for (org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomTypeResponse.JinjiangRoomTypeData roomTypeData : roomTypeDataList) {
            if (validateRoomTypeData(roomTypeData)) {
                // 去重：使用房型编码作为唯一键
                if (!seenCodes.contains(roomTypeData.getRoomTypeCode())) {
                    seenCodes.add(roomTypeData.getRoomTypeCode());
                    validRoomTypes.add(roomTypeData);
                } else {
                    duplicateCount++;
                    log.debug("发现重复的房型编码: {}", roomTypeData.getRoomTypeCode());
                }
            } else {
                invalidCount++;
            }
        }

        if (invalidCount > 0 || duplicateCount > 0) {
            log.warn("房型数据处理完成，有效: {}, 无效: {}, 重复: {}", 
                validRoomTypes.size(), invalidCount, duplicateCount);
        }

        log.info("房型数据列表验证完成，有效数据: {}, 无效数据: {}, 重复数据: {}", 
            validRoomTypes.size(), invalidCount, duplicateCount);
        return validRoomTypes;
    }

    /**
     * 验证房态数据
     * 
     * @param roomTypeList 房型列表
     * @return 验证结果
     */
    public static boolean validateRoomStatusData(org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomStatusResponse.JinjiangRoomTypeList roomTypeList) {
        if (roomTypeList == null) {
            log.warn("房型列表为空");
            return false;
        }

        // 验证房型编码
        if (!isValidRoomTypeCode(roomTypeList.getRoomTypeCode())) {
            log.warn("房型编码无效: {}", roomTypeList.getRoomTypeCode());
            return false;
        }

        // 验证房型名称
        if (!isValidRoomTypeName(roomTypeList.getRoomTypeName())) {
            log.warn("房型名称无效: {}", roomTypeList.getRoomTypeName());
            return false;
        }

        // 验证商品列表
        if (roomTypeList.getProductList() == null || roomTypeList.getProductList().isEmpty()) {
            log.warn("商品列表为空，房型编码: {}", roomTypeList.getRoomTypeCode());
            return false;
        }

        return true;
    }

    /**
     * 验证商品数据
     * 
     * @param product 商品数据
     * @return 验证结果
     */
    public static boolean validateProductData(org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomStatusResponse.JinjiangProduct product) {
        if (product == null) {
            log.warn("商品数据为空");
            return false;
        }

        // 验证商品编码
        if (!isValidProductCode(product.getProductCode())) {
            log.warn("商品编码无效: {}", product.getProductCode());
            return false;
        }

        // 验证商品名称
        if (!isValidProductName(product.getProductName())) {
            log.warn("商品名称无效: {}", product.getProductName());
            return false;
        }

        // 验证可售房量
        if (product.getQuota() != null && product.getQuota() < 0) {
            log.warn("可售房量无效: {}", product.getQuota());
            return false;
        }

        return true;
    }

    /**
     * 验证商品编码
     */
    private static boolean isValidProductCode(String productCode) {
        return productCode != null && 
               !productCode.trim().isEmpty() && 
               productCode.length() >= 3 && 
               productCode.length() <= 20;
    }

    /**
     * 验证商品名称
     */
    private static boolean isValidProductName(String productName) {
        return productName != null && 
               !productName.trim().isEmpty() && 
               productName.length() >= 2 && 
               productName.length() <= 100;
    }

    /**
     * 清理和验证房态数据列表
     * 
     * @param roomTypeList 房型列表
     * @return 验证通过且去重后的房型列表
     */
    public static java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomStatusResponse.JinjiangRoomTypeList> validateAndCleanRoomStatusList(
            java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomStatusResponse.JinjiangRoomTypeList> roomTypeList) {
        
        if (roomTypeList == null || roomTypeList.isEmpty()) {
            log.warn("房型列表为空");
            return new java.util.ArrayList<>();
        }

        java.util.List<org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomStatusResponse.JinjiangRoomTypeList> validRoomTypes = new java.util.ArrayList<>();
        java.util.Set<String> seenCodes = new java.util.HashSet<>();
        int invalidCount = 0;
        int duplicateCount = 0;

        for (org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomStatusResponse.JinjiangRoomTypeList roomType : roomTypeList) {
            if (validateRoomStatusData(roomType)) {
                // 去重：使用房型编码作为唯一键
                if (!seenCodes.contains(roomType.getRoomTypeCode())) {
                    seenCodes.add(roomType.getRoomTypeCode());
                    validRoomTypes.add(roomType);
                } else {
                    duplicateCount++;
                    log.debug("发现重复的房型编码: {}", roomType.getRoomTypeCode());
                }
            } else {
                invalidCount++;
            }
        }

        if (invalidCount > 0 || duplicateCount > 0) {
            log.warn("房态数据处理完成，有效: {}, 无效: {}, 重复: {}", 
                validRoomTypes.size(), invalidCount, duplicateCount);
        }

        log.info("房态数据列表验证完成，有效数据: {}, 无效数据: {}, 重复数据: {}", 
            validRoomTypes.size(), invalidCount, duplicateCount);
        return validRoomTypes;
    }

    /**
     * 私有构造函数，防止实例化
     */
    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
}
