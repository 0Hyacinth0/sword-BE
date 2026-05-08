package org.jeecg.modules.webgame.character.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecg.modules.webgame.character.vo.LevelUpResultVO;

/**
 * @Description: 经验计算工具类
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
public class ExperienceCalculator {

    // ==================== 经验公式参数 ====================
    
    /**最大等级上限*/
    public static final int MAX_LEVEL = 100;
    
    /**每升一级获得的自由属性点数*/
    public static final int POINTS_PER_LEVEL = 3;
    
    /**基础经验值系数*/
    public static final int BASE_EXP = 100;
    
    /**经验增长系数（指数曲线）*/
    public static final double EXP_MULTIPLIER = 1.15;
    
    /**线性增长系数（避免后期过于陡峭）*/
    public static final int EXP_LINEAR = 50;

    /**
     * 计算从当前等级升到下一级所需的总经验值
     * @param level 当前等级
     * @return 升级所需经验值（满级返回 0）
     */
    public static int calculateNextLevelExp(int level) {
        if (level >= MAX_LEVEL) return 0;
        return (int) Math.floor(BASE_EXP * Math.pow(EXP_MULTIPLIER, level) + EXP_LINEAR * level);
    }

    /**
     * 计算增加经验后的升级结果
     * @param currentLevel 当前等级
     * @param currentExp 当前等级内的经验值
     * @param expToAdd 要增加的经验值
     * @return 升级结果
     */
    public static LevelUpResultVO calculateLevelUp(int currentLevel, int currentExp, int expToAdd) {
        int level = currentLevel;
        int exp = currentExp + expToAdd;
        int levelsGained = 0;

        // 循环升级直到经验不足或满级
        while (level < MAX_LEVEL) {
            int nextExp = calculateNextLevelExp(level);
            if (exp < nextExp) break;

            exp -= nextExp;
            level++;
            levelsGained++;
        }

        int pointsGained = levelsGained * POINTS_PER_LEVEL;
        boolean isNewMaxLevel = level == MAX_LEVEL;

        LevelUpResultVO result = new LevelUpResultVO();
        result.setOldLevel(currentLevel);
        result.setNewLevel(level);
        result.setLevelsGained(levelsGained);
        result.setPointsGained(pointsGained);
        result.setOverflowExp(exp);
        result.setIsNewMaxLevel(isNewMaxLevel);

        return result;
    }

    /**
     * 获取当前等级的升级所需经验
     * @param level 当前等级
     * @return 升级所需经验
     */
    public static int getNextLevelExp(int level) {
        return calculateNextLevelExp(level);
    }
}
