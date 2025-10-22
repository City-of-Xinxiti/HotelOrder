package org.xfy.hotelpricecomparison.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xfy.hotelpricecomparison.service.JinjiangApiService;
import org.xfy.hotelpricecomparison.service.JinjiangHotelImageService;
import org.xfy.hotelpricecomparison.dto.request.*;
import org.xfy.hotelpricecomparison.dto.response.JinjiangApiResponse;
import org.xfy.hotelpricecomparison.dto.response.JinjiangBrandResponse;
import org.xfy.hotelpricecomparison.dto.response.JinjiangHotelIdsResponse;
import org.xfy.hotelpricecomparison.dto.response.JinjiangHotelInfoResponse;
import org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomTypeResponse;
import org.xfy.hotelpricecomparison.dto.response.JinjiangHotelRoomStatusResponse;

import java.util.List;
import java.util.ArrayList;

/**
 * 锦江数据同步定时任务
 * 系统启动一分钟后自动请求锦江数据
 */
@Slf4j
@Component
public class JinjiangDataSyncScheduler {

    @Autowired
    private JinjiangApiService jinjiangApiService;
    
    @Autowired
    private JinjiangHotelImageService jinjiangHotelImageService;

    /**
     * 系统启动一分钟后执行锦江数据同步
     * 使用fixedDelay = 60000ms (1分钟) 确保系统完全启动后再执行
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 60000)
    public void syncJinjiangData() {
        log.info("开始执行锦江数据同步任务...");
        
        try {
            // 1. 获取品牌列表
            syncBrandList();
            
            // 2. 获取酒店ID列表
            List<String> hotelIds = syncHotelIds();
            
            if (hotelIds != null && !hotelIds.isEmpty()) {
                // 3. 获取酒店基本信息
                syncHotelInfo(hotelIds);
                
                // 4. 获取酒店房型信息
                syncHotelRoomTypes(hotelIds);
                
                // 5. 获取酒店实时房态
                syncHotelRoomStatus(hotelIds);
                
                // 6. 获取酒店图片
                syncHotelImages(hotelIds);
            }
            
            log.info("锦江数据同步任务执行完成");
            
        } catch (Exception e) {
            log.error("锦江数据同步任务执行失败", e);
        }
    }

    /**
     * 同步品牌列表
     */
    private void syncBrandList() {
        try {
            log.info("开始同步品牌列表...");
            JinjiangBrandListRequest request = new JinjiangBrandListRequest();
            // 设置默认参数
            request.setBrandCode(""); // 空字符串表示获取所有品牌
            request.setLanguageCode(0); // 中文
            
            JinjiangApiResponse<JinjiangBrandResponse> response = jinjiangApiService.getBrandList(request);
            if (response != null && response.getMsgCode() == 0) {
                log.info("品牌列表同步成功，获取到 {} 个品牌", 
                    response.getResult() != null && response.getResult().getBrand() != null ? 
                    response.getResult().getBrand().size() : 0);
            } else {
                log.warn("品牌列表同步失败: {}", response != null ? response.getMessage() : "响应为空");
            }
        } catch (Exception e) {
            log.error("同步品牌列表失败", e);
        }
    }

    /**
     * 同步酒店ID列表
     */
    private List<String> syncHotelIds() {
        try {
            log.info("开始同步酒店ID列表...");
            JinjiangHotelIdsRequest request = new JinjiangHotelIdsRequest();
            request.setPageNum(1);
            request.setPageSize(10);
            
            JinjiangApiResponse<JinjiangHotelIdsResponse> response = jinjiangApiService.getHotelIds(request);
            if (response != null && response.getMsgCode() == 0 && response.getResult() != null) {
                List<String> hotelIds = new ArrayList<>();
                if (response.getResult().getList() != null) {
                    for (JinjiangHotelIdsResponse.JinjiangHotelInfo hotelInfo : response.getResult().getList()) {
                        if (hotelInfo.getInnId() != null) {
                            hotelIds.add(hotelInfo.getInnId());
                        }
                    }
                }
                log.info("酒店ID列表同步成功，获取到 {} 个酒店ID", hotelIds.size());
                return hotelIds;
            } else {
                log.warn("酒店ID列表同步失败: {}", response != null ? response.getMessage() : "响应为空");
                return null;
            }
        } catch (Exception e) {
            log.error("同步酒店ID列表失败", e);
            return null;
        }
    }

    /**
     * 同步酒店基本信息
     */
    private void syncHotelInfo(List<String> hotelIds) {
        try {
            log.info("开始同步酒店基本信息，共 {} 个酒店", hotelIds.size());
            int successCount = 0;
            
            for (String hotelId : hotelIds) {
                try {
                    JinjiangHotelInfoRequest request = new JinjiangHotelInfoRequest();
                    request.setInnId(hotelId);
                    
                    JinjiangApiResponse<JinjiangHotelInfoResponse> response = jinjiangApiService.getHotelInfo(request);
                    if (response != null && response.getMsgCode() == 0) {
                        successCount++;
                        log.debug("酒店 {} 基本信息同步成功", hotelId);
                    } else {
                        log.warn("酒店 {} 基本信息同步失败: {}", hotelId, 
                            response != null ? response.getMessage() : "响应为空");
                    }
                } catch (Exception e) {
                    log.error("同步酒店 {} 基本信息失败", hotelId, e);
                }
            }
            
            log.info("酒店基本信息同步完成，成功 {} 个，失败 {} 个", successCount, hotelIds.size() - successCount);
        } catch (Exception e) {
            log.error("同步酒店基本信息失败", e);
        }
    }

    /**
     * 同步酒店房型信息
     */
    private void syncHotelRoomTypes(List<String> hotelIds) {
        try {
            log.info("开始同步酒店房型信息，共 {} 个酒店", hotelIds.size());
            int successCount = 0;
            
            for (String hotelId : hotelIds) {
                try {
                    JinjiangHotelRoomTypeRequest request = new JinjiangHotelRoomTypeRequest();
                    request.setHotelId(hotelId);
                    
                    JinjiangApiResponse<JinjiangHotelRoomTypeResponse> response = jinjiangApiService.getHotelRoomType(request);
                    if (response != null && response.getMsgCode() == 0) {
                        successCount++;
                        log.debug("酒店 {} 房型信息同步成功", hotelId);
                    } else {
                        log.warn("酒店 {} 房型信息同步失败: {}", hotelId, 
                            response != null ? response.getMessage() : "响应为空");
                    }
                } catch (Exception e) {
                    log.error("同步酒店 {} 房型信息失败", hotelId, e);
                }
            }
            
            log.info("酒店房型信息同步完成，成功 {} 个，失败 {} 个", successCount, hotelIds.size() - successCount);
        } catch (Exception e) {
            log.error("同步酒店房型信息失败", e);
        }
    }

    /**
     * 同步酒店实时房态
     */
    private void syncHotelRoomStatus(List<String> hotelIds) {
        try {
            log.info("开始同步酒店实时房态，共 {} 个酒店，查询14天数据", hotelIds.size());
            int successCount = 0;
            
            for (String hotelId : hotelIds) {
                try {
                    JinjiangHotelRoomStatusRequest request = new JinjiangHotelRoomStatusRequest();
                    request.setInnId(hotelId);
                    // 设置查询日期范围（今天到14天后）
                    request.setEndOfDay(java.time.LocalDate.now().plusDays(14).toString());
                    request.setDays(14);
                    
                    JinjiangApiResponse<JinjiangHotelRoomStatusResponse> response = jinjiangApiService.getHotelRealRoomStatus(request);
                    if (response != null && response.getMsgCode() == 0) {
                        successCount++;
                        log.debug("酒店 {} 实时房态同步成功", hotelId);
                    } else {
                        log.warn("酒店 {} 实时房态同步失败: {}", hotelId, 
                            response != null ? response.getMessage() : "响应为空");
                    }
                } catch (Exception e) {
                    log.error("同步酒店 {} 实时房态失败", hotelId, e);
                }
            }
            
            log.info("酒店实时房态同步完成，成功 {} 个，失败 {} 个", successCount, hotelIds.size() - successCount);
        } catch (Exception e) {
            log.error("同步酒店实时房态失败", e);
        }
    }

    /**
     * 同步酒店图片
     */
    private void syncHotelImages(List<String> hotelIds) {
        try {
            log.info("开始同步酒店图片，共 {} 个酒店", hotelIds.size());
            int successCount = 0;
            
            for (String hotelId : hotelIds) {
                try {
                    // 同步酒店图片数据（中文）
                    boolean success = jinjiangHotelImageService.syncHotelImages(hotelId, 0);
                    if (success) {
                        successCount++;
                        log.debug("酒店 {} 图片同步成功", hotelId);
                    } else {
                        log.warn("酒店 {} 图片同步失败", hotelId);
                    }
                    
                } catch (Exception e) {
                    log.error("同步酒店 {} 图片失败", hotelId, e);
                }
            }
            
            log.info("酒店图片同步完成，成功 {} / {} 个酒店", successCount, hotelIds.size());
        } catch (Exception e) {
            log.error("同步酒店图片失败", e);
        }
    }
}
