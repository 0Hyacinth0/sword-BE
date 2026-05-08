package org.jeecg.modules.webgame.character.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @Description: 装备信息VO
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
public class EquipmentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**装备ID*/
    private String id;
    
    /**装备名称*/
    private String name;
    
    /**稀有度：Normal/Rare/Epic/Legendary*/
    private String rarity;
    
    /**槽位类型：weapon/helmet/chest/legs/accessory1/accessory2*/
    private String slotType;
    
    /**装备属性加成*/
    private Map<String, Object> stats;
    
    /**套装ID（可选）*/
    private String setId;
    
    /**套装名称（可选）*/
    private String setName;
}
