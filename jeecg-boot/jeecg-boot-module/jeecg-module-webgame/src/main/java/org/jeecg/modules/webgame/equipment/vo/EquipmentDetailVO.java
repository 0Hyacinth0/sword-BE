package org.jeecg.modules.webgame.equipment.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description: 装备详情VO（包含套装信息）
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Data
public class EquipmentDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**装备ID*/
    private String id;
    
    /**装备名称*/
    private String name;
    
    /**稀有度(Normal/Rare/Epic/Legendary)*/
    private String rarity;
    
    /**槽位类型(weapon/helmet/chest/legs/accessory1/accessory2)*/
    private String slotType;
    
    /**装备属性*/
    private Map<String, Object> stats;
    
    /**套装ID*/
    private String setId;
    
    /**套装名称*/
    private String setName;
    
    /**图标URL*/
    private String iconUrl;
    
    /**装备描述*/
    private String description;
    
    /**等级需求*/
    private Integer levelRequirement;
    
    /**强化等级*/
    private Integer enhanceLevel;
    
    /**额外属性（JSON格式）*/
    private Map<String, Object> extraStats;
}
