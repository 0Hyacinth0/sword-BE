package org.jeecg.modules.webgame.character.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @Description: 角色信息VO
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
public class CharacterVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    private String id;
    
    /**用户ID*/
    private String userId;
    
    /**角色名称*/
    private String characterName;
    
    /**职业类型(1-战士,2-法师,3-猎人)*/
    private Integer profession;
    
    /**职业名称*/
    private String professionName;
    
    /**等级*/
    private Integer level;
    
    /**经验值*/
    private Long experience;
    
    /**下一级别所需经验*/
    private Long nextLevelExp;
    
    /**可用属性点*/
    private Integer availablePoints;
    
    /**力量*/
    private Integer strength;
    
    /**智力*/
    private Integer intelligence;
    
    /**敏捷*/
    private Integer agility;
    
    /**生命值*/
    private Integer hp;
    
    /**最大生命值*/
    private Integer maxHp;
    
    /**魔法值*/
    private Integer mp;
    
    /**最大魔法值*/
    private Integer maxMp;
    
    /**物理攻击力*/
    private Integer physicalAttack;
    
    /**魔法攻击力*/
    private Integer magicAttack;
    
    /**防御力*/
    private Integer defense;
    
    /**闪避率*/
    private Double dodgeRate;
    
    /**暴击率*/
    private Double criticalRate;
    
    /**战宠加成-生命值*/
    private Integer bonusHp;
    
    /**战宠加成-物理攻击*/
    private Integer bonusPhysicalAttack;
    
    /**战宠加成-魔法攻击*/
    private Integer bonusMagicAttack;
    
    /**战宠加成-防御力*/
    private Integer bonusDefense;
    
    /**装备信息（6个槽位）*/
    private EquipmentInfo equipment;
    
    /**出战战宠信息*/
    private PetInfoVO activePet;
    
    /**创建时间*/
    private java.util.Date createTime;
    
    /**更新时间*/
    private java.util.Date updateTime;
    
    /**角色头像URL（小图，用于角色列表卡片）*/
    private String avatarUrl;
    
    /**角色立绘URL（大图，用于角色详情面板）*/
    private String portraitUrl;
    
    /**
     * 装备槽位信息内部类
     */
    @Data
    public static class EquipmentInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**武器槽位*/
        private EquipmentVO weapon;
        
        /**头盔槽位*/
        private EquipmentVO helmet;
        
        /**胸甲槽位*/
        private EquipmentVO chest;
        
        /**护腿槽位*/
        private EquipmentVO legs;
        
        /**饰品槽位1*/
        private EquipmentVO accessory1;
        
        /**饰品槽位2*/
        private EquipmentVO accessory2;
    }
}
