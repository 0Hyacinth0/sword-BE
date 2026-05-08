package org.jeecg.modules.webgame.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 物品模板表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_item_template")
@EqualsAndHashCode(callSuper = false)
public class WgItemTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**物品名称*/
    private String itemName;
    
    /**物品类型(1-装备,2-消耗品,3-材料)*/
    private Integer itemType;
    
    /**物品分类(consumable/material/equipment)*/
    private String category;
    
    /**稀有度(Normal/Rare/Epic/Legendary)*/
    private String rarity;
    
    /**最大堆叠数量*/
    private Integer maxStack;
    
    /**装备部位(1-武器,2-头部,3-胸部,4-腿部,5-饰品)*/
    private Integer equipSlot;
    
    /**品质(1-普通白色,2-稀有蓝色,3-史诗紫色,4-传说橙色)*/
    private Integer quality;
    
    /**基础属性加成百分比*/
    private Double baseStatPercent;
    
    /**附加词条数*/
    private Integer affixCount;
    
    /**是否特殊被动*/
    private Integer hasSpecialPassive;
    
    /**物品描述*/
    private String description;
    
    /**图标URL*/
    private String iconUrl;
    
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
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
