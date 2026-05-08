package org.jeecg.modules.webgame.character.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @Description: 角色属性计算器单元测试
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
public class CharacterStatsCalculatorTest {

    /**
     * 测试战士1级初始属性
     * 基础属性: 力量=10, 智力=3, 敏捷=5
     */
    @Test
    public void testWarriorLevel1() {
        CharacterStatsCalculator.DerivedStats stats = 
            CharacterStatsCalculator.calculateBaseStats(10, 3, 5, 1);
        
        // HP = (80 + 10*5) * 1.2 = 130 * 1.2 = 156
        assertEquals(156, stats.getMaxHp());
        
        // MP = (20 + 3*5) * 0.8 = 35 * 0.8 = 28
        assertEquals(28, stats.getMaxMp());
        
        // 物攻 = (10*2 + 5*0.5) * 1.15 = 22.5 * 1.15 = 25.875 -> 25
        assertEquals(25, stats.getPhysicalAttack());
        
        // 魔攻 = 3*2 * 0.8 = 6 * 0.8 = 4.8 -> 4
        assertEquals(4, stats.getMagicAttack());
        
        // 防御 = 5 + 3 = 8
        assertEquals(8, stats.getDefense());
        
        // 闪避率 = 5 * 0.005 = 0.025
        assertEquals(0.025, stats.getDodgeRate(), 0.0001);
        
        // 暴击率 = 5 * 0.004 = 0.02
        assertEquals(0.02, stats.getCriticalRate(), 0.0001);
        
        System.out.println("战士1级属性: " + stats);
    }

    /**
     * 测试法师1级初始属性
     * 基础属性: 力量=2, 智力=12, 敏捷=4
     */
    @Test
    public void testMageLevel1() {
        CharacterStatsCalculator.DerivedStats stats = 
            CharacterStatsCalculator.calculateBaseStats(2, 12, 4, 2);
        
        // HP = (80 + 2*5) * 0.8 = 90 * 0.8 = 72
        assertEquals(72, stats.getMaxHp());
        
        // MP = (20 + 12*5) * 1.3 = 80 * 1.3 = 104
        assertEquals(104, stats.getMaxMp());
        
        // 物攻 = (2*2 + 4*0.5) * 0.7 = 6 * 0.7 = 4.2 -> 4
        assertEquals(4, stats.getPhysicalAttack());
        
        // 魔攻 = 12*2 * 1.2 = 24 * 1.2 = 28.8 -> 28
        assertEquals(28, stats.getMagicAttack());
        
        // 防御 = 5 + 0 = 5
        assertEquals(5, stats.getDefense());
        
        // 闪避率 = 4 * 0.005 = 0.02
        assertEquals(0.02, stats.getDodgeRate(), 0.0001);
        
        // 暴击率 = 4 * 0.004 = 0.016
        assertEquals(0.016, stats.getCriticalRate(), 0.0001);
        
        System.out.println("法师1级属性: " + stats);
    }

    /**
     * 测试猎人1级初始属性
     * 基础属性: 力量=5, 智力=4, 敏捷=11
     */
    @Test
    public void testHunterLevel1() {
        CharacterStatsCalculator.DerivedStats stats = 
            CharacterStatsCalculator.calculateBaseStats(5, 4, 11, 3);
        
        // HP = (80 + 5*5) * 0.9 = 105 * 0.9 = 94.5 -> 94
        assertEquals(94, stats.getMaxHp());
        
        // MP = (20 + 4*5) * 0.9 = 40 * 0.9 = 36
        assertEquals(36, stats.getMaxMp());
        
        // 物攻 = (5*2 + 11*0.5) * 1.0 = 15.5 * 1.0 = 15.5 -> 15
        assertEquals(15, stats.getPhysicalAttack());
        
        // 魔攻 = 4*2 * 0.9 = 8 * 0.9 = 7.2 -> 7
        assertEquals(7, stats.getMagicAttack());
        
        // 防御 = 5 + 1 = 6
        assertEquals(6, stats.getDefense());
        
        // 闪避率 = 11 * 0.005 = 0.055
        assertEquals(0.055, stats.getDodgeRate(), 0.0001);
        
        // 暴击率 = 11 * 0.004 = 0.044
        assertEquals(0.044, stats.getCriticalRate(), 0.0001);
        
        System.out.println("猎人1级属性: " + stats);
    }

    /**
     * 测试加点后的属性变化
     * 战士从1级升到2级，获得5点属性点，全部加到力量上
     * 原属性: 力量=10, 智力=3, 敏捷=5
     * 新属性: 力量=15, 智力=3, 敏捷=5
     */
    @Test
    public void testWarriorAfterLevelUp() {
        CharacterStatsCalculator.DerivedStats stats = 
            CharacterStatsCalculator.calculateBaseStats(15, 3, 5, 1);
        
        // HP = (80 + 15*5) * 1.2 = 155 * 1.2 = 186
        assertEquals(186, stats.getMaxHp());
        
        // MP = (20 + 3*5) * 0.8 = 35 * 0.8 = 28 (不变)
        assertEquals(28, stats.getMaxMp());
        
        // 物攻 = (15*2 + 5*0.5) * 1.15 = 32.5 * 1.15 = 37.375 -> 37
        assertEquals(37, stats.getPhysicalAttack());
        
        // 魔攻 = 3*2 * 0.8 = 6 * 0.8 = 4.8 -> 4 (不变)
        assertEquals(4, stats.getMagicAttack());
        
        // 防御 = 5 + 3 = 8 (不变)
        assertEquals(8, stats.getDefense());
        
        // 闪避率 = 5 * 0.005 = 0.025 (不变)
        assertEquals(0.025, stats.getDodgeRate(), 0.0001);
        
        // 暴击率 = 5 * 0.004 = 0.02 (不变)
        assertEquals(0.02, stats.getCriticalRate(), 0.0001);
        
        System.out.println("战士加点后属性: " + stats);
    }

    /**
     * 测试职业加成配置
     */
    @Test
    public void testProfessionBonus() {
        // 战士
        CharacterStatsCalculator.ProfessionBonus warrior = 
            CharacterStatsCalculator.getProfessionBonus(1);
        assertEquals(1.2, warrior.getHpMultiplier(), 0.0001);
        assertEquals(0.8, warrior.getMpMultiplier(), 0.0001);
        assertEquals(1.15, warrior.getPhysicalAttackMultiplier(), 0.0001);
        assertEquals(0.8, warrior.getMagicAttackMultiplier(), 0.0001);
        assertEquals(3, warrior.getDefenseBonus());
        
        // 法师
        CharacterStatsCalculator.ProfessionBonus mage = 
            CharacterStatsCalculator.getProfessionBonus(2);
        assertEquals(0.8, mage.getHpMultiplier(), 0.0001);
        assertEquals(1.3, mage.getMpMultiplier(), 0.0001);
        assertEquals(0.7, mage.getPhysicalAttackMultiplier(), 0.0001);
        assertEquals(1.2, mage.getMagicAttackMultiplier(), 0.0001);
        assertEquals(0, mage.getDefenseBonus());
        
        // 猎人
        CharacterStatsCalculator.ProfessionBonus hunter = 
            CharacterStatsCalculator.getProfessionBonus(3);
        assertEquals(0.9, hunter.getHpMultiplier(), 0.0001);
        assertEquals(0.9, hunter.getMpMultiplier(), 0.0001);
        assertEquals(1.0, hunter.getPhysicalAttackMultiplier(), 0.0001);
        assertEquals(0.9, hunter.getMagicAttackMultiplier(), 0.0001);
        assertEquals(1, hunter.getDefenseBonus());
        
        System.out.println("所有职业加成配置正确");
    }
}
