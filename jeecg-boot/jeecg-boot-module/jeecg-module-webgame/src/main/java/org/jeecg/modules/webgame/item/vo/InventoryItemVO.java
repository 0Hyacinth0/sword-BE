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
    
    /**物品模板ID*/
    private Integer itemId;
    
    /**持有数量*/
    private Integer quantity;
    
    /**获取时间*/
    private Date obtainedAt;
    
    // ==================== 物品模板信息 ====================
    
    /**物品名称*/
    private String name;
    
    /**物品分类(consumable/material/equipment)*/
    private String category;
    
    /**稀有度(Normal/Rare/Epic/Legendary)*/
    private String rarity;
    
    /**物品描述*/
    private String description;
    
    /**图标URL*/
    private String iconUrl;
    
    /**最大堆叠数量*/
    private Integer maxStack;
    
    /**出售价格*/
    private Integer sellPrice;
    
    // ==================== 消耗品特有字段 ====================
    
    /**效果类型(heal_hp/heal_mp/add_exp/revive)*/
    private String effectType;
    
    /**效果数值*/
    private Integer effectValue;
    
    // ==================== 材料特有字段 ====================
    
    /**获取途径描述*/
    private String source;
    
    /**用途描述*/
    private String usage;
}
