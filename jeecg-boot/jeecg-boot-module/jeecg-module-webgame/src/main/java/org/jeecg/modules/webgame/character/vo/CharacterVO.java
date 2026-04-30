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
}
