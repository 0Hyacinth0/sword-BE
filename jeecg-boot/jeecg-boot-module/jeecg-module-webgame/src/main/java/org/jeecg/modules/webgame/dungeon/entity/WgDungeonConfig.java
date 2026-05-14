package org.jeecg.modules.webgame.dungeon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 副本配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
@TableName("wg_dungeon_configs")
@EqualsAndHashCode(callSuper = false)
public class WgDungeonConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**副本ID*/
    @TableId
    private String id;
    
    /**副本名称*/
    private String name;
    
    /**所属区域ID*/
    private String areaId;
    
    /**难度(normal/elite)*/
    private String difficulty;
    
    /**体力消耗*/
    private Integer staminaCost;
    
    /**等级要求*/
    private Integer levelRequirement;
    
    /**总层数*/
    private Integer totalFloors;
    
    /**通关经验奖励*/
    private Integer bonusExp;
    
    /**通关金币奖励*/
    private Integer bonusGold;
    
    /**通关保底物品JSON数组*/
    private String guaranteedItems;
    
    /**是否为精英副本*/
    private Boolean isElite;
    
    /**精英属性倍率*/
    private BigDecimal eliteStatMultiplier;
    
    /**精英技能组ID*/
    private Integer eliteSkillGroupId;
    
    /**排序顺序*/
    private Integer sortOrder;
}
