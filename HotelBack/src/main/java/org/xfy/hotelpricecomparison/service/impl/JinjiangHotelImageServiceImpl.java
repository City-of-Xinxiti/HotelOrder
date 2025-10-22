package org.xfy.hotelpricecomparison.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xfy.hotelpricecomparison.dto.request.JinjiangHotelImageRequest;
import org.xfy.hotelpricecomparison.dto.response.JinjiangHotelImageResponse;
import org.xfy.hotelpricecomparison.entity.JinjiangHotelImage;
import org.xfy.hotelpricecomparison.mapper.JinjiangHotelImageMapper;
import org.xfy.hotelpricecomparison.service.JinjiangApiService;
import org.xfy.hotelpricecomparison.service.JinjiangHotelImageService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 锦江酒店图片服务实现类
 */
@Slf4j
@Service
public class JinjiangHotelImageServiceImpl extends ServiceImpl<JinjiangHotelImageMapper, JinjiangHotelImage> 
        implements JinjiangHotelImageService {
    
    @Autowired
    private JinjiangApiService jinjiangApiService;
    
    @Override
    public List<JinjiangHotelImage> getImagesByInnIdAndType(String innId, Integer imageType, Integer languageCode) {
        log.info("查询酒店图片，酒店ID: {}, 图片类型: {}, 语言类型: {}", innId, imageType, languageCode);
        return baseMapper.selectByInnIdAndImageType(innId, imageType, languageCode);
    }
    
    @Override
    public List<JinjiangHotelImage> getImagesByInnIdAndRoomType(String innId, String roomTypeCode, Integer languageCode) {
        log.info("查询房型图片，酒店ID: {}, 房型编码: {}, 语言类型: {}", innId, roomTypeCode, languageCode);
        return baseMapper.selectByInnIdAndRoomType(innId, roomTypeCode, languageCode);
    }
    
    @Override
    public List<JinjiangHotelImage> getMasterImagesByInnId(String innId, Integer languageCode) {
        log.info("查询酒店主图，酒店ID: {}, 语言类型: {}", innId, languageCode);
        return baseMapper.selectMasterImagesByInnId(innId, languageCode);
    }
    
    @Override
    @Transactional
    public boolean syncHotelImages(String innId, Integer languageCode) {
        try {
            log.info("开始同步酒店图片，酒店ID: {}, 语言类型: {}", innId, languageCode);
            
            // 调用锦江API获取图片数据
            JinjiangHotelImageRequest request = new JinjiangHotelImageRequest();
            request.setInnId(innId);
            request.setLangType(languageCode != null ? languageCode.toString() : "0");
            
            var response = jinjiangApiService.getHotelImages(request);
            if (response == null || response.getMsgCode() != 0 || response.getResult() == null) {
                log.warn("获取酒店图片失败，酒店ID: {}, 响应: {}", innId, response);
                return false;
            }
            
            JinjiangHotelImageResponse imageResponse = response.getResult();
            if (imageResponse.getImageDatas() == null || imageResponse.getImageDatas().isEmpty()) {
                log.info("酒店图片数据为空，酒店ID: {}", innId);
                return true;
            }
            
            // 先删除该酒店的所有图片数据
            QueryWrapper<JinjiangHotelImage> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("inn_id", innId);
            if (languageCode != null) {
                deleteWrapper.eq("language_code", languageCode);
            }
            baseMapper.delete(deleteWrapper);
            
            // 批量插入新的图片数据
            int successCount = 0;
            for (JinjiangHotelImageResponse.ImageData imageData : imageResponse.getImageDatas()) {
                try {
                    JinjiangHotelImage hotelImage = new JinjiangHotelImage();
                    hotelImage.setInnId(innId);
                    hotelImage.setImageDes(imageData.getImageDes());
                    hotelImage.setImageType(imageData.getImageType());
                    hotelImage.setImageUrl(imageData.getImageUrl());
                    hotelImage.setMaster(imageData.getMaster());
                    hotelImage.setRoomTypeCode(imageData.getRoomTypeCode());
                    hotelImage.setSizeType(imageData.getSizeType());
                    hotelImage.setUploadFileName(imageData.getUploadFileName());
                    hotelImage.setLanguageCode(languageCode != null ? languageCode : 0);
                    hotelImage.setValid(1);
                    hotelImage.setSortOrder(0);
                    hotelImage.setCreatedAt(LocalDateTime.now());
                    hotelImage.setUpdatedAt(LocalDateTime.now());
                    
                    baseMapper.insert(hotelImage);
                    successCount++;
                } catch (Exception e) {
                    log.error("插入酒店图片失败，酒店ID: {}, 图片URL: {}", innId, imageData.getImageUrl(), e);
                }
            }
            
            log.info("酒店图片同步完成，酒店ID: {}, 成功: {}/{}", innId, successCount, imageResponse.getImageDatas().size());
            return successCount > 0;
            
        } catch (Exception e) {
            log.error("同步酒店图片失败，酒店ID: {}", innId, e);
            return false;
        }
    }
}
