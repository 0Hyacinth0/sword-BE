package org.jeecg.modules.webgame.character.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description: 角色属性计算工具类
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
public class CharacterStatsCalculator {

    // ==================== 公式参数常量 ====================
    
    /** 1 点力量 = 5 HP */
    private static final int STR_TO_HP = 5;
    
    /** 1 点智力 = 5 MP */
    private static final int INT_TO_MP = 5;
    
    /** 1 点力量 = 2 物攻 */
    private static final double STR_TO_PHYSICAL_ATTACK = 2;
    
    /** 1 点智力 = 2 魔攻 */
    private static final double INT_TO_MAGIC_ATTACK = 2;
    
    /** 1 点敏捷 = 0.5 物攻 */
    private static final double AGI_TO_PHYSICAL_ATTACK = 0.5;
    
    /** 1 点敏捷 = 0.5% 闪避率 */
    private static final double AGI_TO_DODGE = 0.005;
    
    /** 1 点敏捷 = 0.4% 暴击率 */
    private static final double AGI_TO_CRITICAL = 0.004;
    
    /** HP 基础值 */
    private static final int BASE_HP = 80;
    
    /** MP 基础值 */
    private static final int BASE_MP = 20;
    
    /** 防御基础值 */
    private static final int BASE_DEFENSE = 5;

    // ==================== 职业加成配置 ====================

    /**
     * 获取职业加成配置
     * @param profession 职业编号：1-战士, 2-法师, 3-猎人
     * @return 职业加成对象
     */
    public static ProfessionBonus getProfessionBonus(int profession) {
        switch (profession) {
            case 1: // 战士
                return new ProfessionBonus(1.2, 0.8, 1.15, 0.8, 3);
            case 2: // 法师
                return new ProfessionBonus(0.8, 1.3, 0.7, 1.2, 0);
            case 3: // 猎人
                return new ProfessionBonus(0.9, 0.9, 1.0, 0.9, 1);
            default:
                return new ProfessionBonus(1.0, 1.0, 1.0, 1.0, 0);
        }
    }

    /**
     * 计算角色基础衍生属性（不含装备和战宠加成）
     * @param str 力量
     * @param intelligence 智力
     * @param agi 敏捷
     * @param profession 职业编号：1-战士, 2-法师, 3-猎人
     * @return 衍生属性对象
     */
    public static DerivedStats calculateBaseStats(int str, int intelligence, int agi, int profession) {
        // 获取职业加成配置
        ProfessionBonus bonus = getProfessionBonus(profession);
        
        // HP = (基础值 + 力量×5) × 职业倍率
        int maxHp = (int) Math.floor((BASE_HP + str * STR_TO_HP) * bonus.getHpMultiplier());
        
        // MP = (基础值 + 智力×5) × 职业倍率
        int maxMp = (int) Math.floor((BASE_MP + intelligence * INT_TO_MP) * bonus.getMpMultiplier());
        
        // 物攻 = (力量×2 + 敏捷×0.5) × 职业倍率
        int physicalAttack = (int) Math.floor(
            (str * STR_TO_PHYSICAL_ATTACK + agi * AGI_TO_PHYSICAL_ATTACK) 
            * bonus.getPhysicalAttackMultiplier()
        );
        
        // 魔攻 = 智力×2 × 职业倍率
        int magicAttack = (int) Math.floor(
            (intelligence * INT_TO_MAGIC_ATTACK) * bonus.getMagicAttackMultiplier()
        );
        
        // 防御 = 基础值 + 职业额外加成
        int defense = BASE_DEFENSE + bonus.getDefenseBonus();
        
        // 闪避率 = 敏捷×0.005（百分比）
        double dodgeRate = agi * AGI_TO_DODGE;
        
        // 暴击率 = 敏捷×0.004（百分比）
        double criticalRate = agi * AGI_TO_CRITICAL;
        
        return new DerivedStats(maxHp, maxMp, physicalAttack, magicAttack, defense, dodgeRate, criticalRate);
    }

    /**
     * 职业加成配置类
     */
    @Data
    @AllArgsConstructor
    public static class ProfessionBonus {
        /** HP 倍率 */
        private double hpMultiplier;
        
        /** MP 倍率 */
        private double mpMultiplier;
        
        /** 物理攻击倍率 */
        private double physicalAttackMultiplier;
        
        /** 魔法攻击倍率 */
        private double magicAttackMultiplier;
        
        /** 防御额外加成 */
        private int defenseBonus;
    }

    /**
     * 衍生属性类
     */
    @Data
    @AllArgsConstructor
    public static class DerivedStats {
        /** 最大生命值 */
        private int maxHp;
        
        /** 最大魔法值 */
        private int maxMp;
        
        /** 物理攻击力 */
        private int physicalAttack;
        
        /** 魔法攻击力 */
        private int magicAttack;
        
        /** 防御力 */
        private int defense;
        
        /** 闪避率（小数形式，如 0.05 表示 5%） */
        private double dodgeRate;
        
        /** 暴击率（小数形式，如 0.03 表示 3%） */
        private double criticalRate;
    }
}
