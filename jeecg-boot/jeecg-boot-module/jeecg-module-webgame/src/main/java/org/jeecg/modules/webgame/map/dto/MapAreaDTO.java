package org.jeecg.modules.webgame.map.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 地图区域DTO（返回给前端）
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
public class MapAreaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**区域ID*/
    private String id;
    
    /**区域名称*/
    private String name;
    
    /**emoji图标*/
    private String icon;
    
    /**等级范围[min, max]*/
    private Integer[] levelRange;
    
    /**区域描述*/
    private String description;
    
    /**解锁所需等级*/
    private Integer unlockLevel;
    
    /**怪物列表*/
    private List<AreaMonsterDTO> monsters;
    
    /**掉落预览*/
    private List<AreaDropDTO> drops;
}
