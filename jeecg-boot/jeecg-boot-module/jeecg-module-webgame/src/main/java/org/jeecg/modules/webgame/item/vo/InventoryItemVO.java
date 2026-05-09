package org.jeecg.modules.webgame.item.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 背包物品VO（包含物品模板信息）
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Data
public class InventoryItemVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**背包记录ID*/
    private String id;
    
    /**角色ID*/
    private String characterId;
    
    /**物品模板ID（varchar 36）*/
    private String itemId;
    
    /**持有数量*/
    private Integer quantity;
    
    /**获取时间*/
    private Date obtainedAt;
    
    // ==================== 物品模板信息 ====================
    
    /**物品名称*/
    private String name;
    
    /**物品类型*/
    private Integer type;
    
    /**装备部位类型*/
    private String slotType;
    
    /**稀有度*/
    private String rarity;
    
    /**基础属性加成（JSON）*/
    private String baseStats;
    
    /**套装ID*/
    private String setId;
    
    /**套装名称*/
    private String setName;
    
    /**图标URL*/
    private String icon;
    
    /**物品描述*/
    private String description;
}
