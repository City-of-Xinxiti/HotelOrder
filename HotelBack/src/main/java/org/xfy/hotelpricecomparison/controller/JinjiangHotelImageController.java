package org.xfy.hotelpricecomparison.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xfy.hotelpricecomparison.entity.JinjiangHotelImage;
import org.xfy.hotelpricecomparison.service.JinjiangHotelImageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 锦江酒店图片控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/hotel/images")
@CrossOrigin(origins = "*")
public class JinjiangHotelImageController {
    
    @Autowired
    private JinjiangHotelImageService jinjiangHotelImageService;
    
    /**
     * 根据酒店ID和图片类型查询图片
     */
    @GetMapping("/{innId}")
    public ResponseEntity<Map<String, Object>> getHotelImages(
            @PathVariable String innId,
            @RequestParam(required = false) Integer imageType,
            @RequestParam(required = false, defaultValue = "0") Integer languageCode) {
        
        try {
            log.info("查询酒店图片，酒店ID: {}, 图片类型: {}, 语言类型: {}", innId, imageType, languageCode);
            
            List<JinjiangHotelImage> images;
            if (imageType != null) {
                images = jinjiangHotelImageService.getImagesByInnIdAndType(innId, imageType, languageCode);
            } else {
                images = jinjiangHotelImageService.getMasterImagesByInnId(innId, languageCode);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 0);
            response.put("message", "success");
            response.put("result", images);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("查询酒店图片失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", -1);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * 根据酒店ID和房型编码查询图片
     */
    @GetMapping("/{innId}/room-type/{roomTypeCode}")
    public ResponseEntity<Map<String, Object>> getRoomTypeImages(
            @PathVariable String innId,
            @PathVariable String roomTypeCode,
            @RequestParam(required = false, defaultValue = "0") Integer languageCode) {
        
        try {
            log.info("查询房型图片，酒店ID: {}, 房型编码: {}, 语言类型: {}", innId, roomTypeCode, languageCode);
            
            List<JinjiangHotelImage> images = jinjiangHotelImageService.getImagesByInnIdAndRoomType(
                innId, roomTypeCode, languageCode);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 0);
            response.put("message", "success");
            response.put("result", images);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("查询房型图片失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", -1);
            response.put("message", "查询失败: " + e.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * 同步酒店图片数据
     */
    @PostMapping("/sync/{innId}")
    public ResponseEntity<Map<String, Object>> syncHotelImages(
            @PathVariable String innId,
            @RequestParam(required = false, defaultValue = "0") Integer languageCode) {
        
        try {
            log.info("同步酒店图片，酒店ID: {}, 语言类型: {}", innId, languageCode);
            
            boolean success = jinjiangHotelImageService.syncHotelImages(innId, languageCode);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", success ? 0 : -1);
            response.put("message", success ? "同步成功" : "同步失败");
            response.put("result", success);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("同步酒店图片失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", -1);
            response.put("message", "同步失败: " + e.getMessage());
            response.put("result", false);
            return ResponseEntity.ok(response);
        }
    }
}
