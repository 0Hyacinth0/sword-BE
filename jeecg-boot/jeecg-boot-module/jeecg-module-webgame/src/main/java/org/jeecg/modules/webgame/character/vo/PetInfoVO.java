package org.jeecg.modules.webgame.character.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @Description: 战宠信息VO
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
public class PetInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**战宠实例ID*/
    private String id;
    
    /**战宠类型ID*/
    private Integer petTypeId;
    
    /**战宠昵称*/
    private String nickname;
    
    /**战宠等级*/
    private Integer level;
    
    /**当前经验*/
    private Integer exp;
    
    /**升级所需经验*/
    private Integer maxExp;
    
    /**稀有度：1-N, 2-R, 3-SR, 4-SSR*/
    private Integer rarity;
    
    /**是否出战*/
    private Boolean isActive;
    
    /**战宠属性*/
    private PetStatsVO stats;
    
    /**给主人的属性加成*/
    private PetBonusVO bonusToOwner;
    
    /**
     * 战宠属性内部类
     */
    @Data
    public static class PetStatsVO implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**当前生命值*/
        private Integer hp;
        
        /**最大生命值*/
        private Integer maxHp;
        
        /**攻击力*/
        private Integer attack;
        
        /**防御力*/
        private Integer defense;
        
        /**速度*/
        private Integer speed;
    }
    
    /**
     * 主人加成内部类
     */
    @Data
    public static class PetBonusVO implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**给主人加的生命值*/
        private Integer hp;
        
        /**给主人加的攻击力*/
        private Integer attack;
        
        /**给主人加的防御力*/
        private Integer defense;
        
        /**给主人加的暴击率*/
        private Double criticalRate;
        
        /**给主人加的闪避率*/
        private Double dodgeRate;
    }
}
