package org.jeecg.modules.webgame.map.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.webgame.map.dto.MapAreaDTO;
import org.jeecg.modules.webgame.map.entity.WgMapArea;

import java.util.List;

/**
 * @Description: 地图区域Service
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
public interface IWgMapAreaService extends IService<WgMapArea> {
    
    /**
     * 获取所有区域列表（含怪物和掉落信息）
     */
    List<MapAreaDTO> getAllAreas();
    
    /**
     * 获取单个区域详情
     */
    MapAreaDTO getAreaById(String areaId);
}
