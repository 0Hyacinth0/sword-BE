package org.jeecg.modules.webgame.battle.util;

import org.jeecg.modules.webgame.battle.vo.BattleStateVO;
import org.jeecg.modules.webgame.battle.vo.BuffEffectVO;
import org.jeecg.modules.webgame.battle.vo.CombatantVO;

import java.util.*;

/**
 * @Description: 战斗引擎工具类
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
public class BattleEngine {

    /**
     * 计算伤害
     * @param attacker 攻击者
     * @param defender 防御者
     * @param skillPower 技能威力倍率（100表示100%）
     * @return 伤害结果
     */
    public static DamageResult calculateDamage(CombatantVO attacker, CombatantVO defender, int skillPower) {
        DamageResult result = new DamageResult();
        result.setTargetUid(defender.getUid());
        result.setSourceType("skill");

        // 1. 闪避判定
        double dodgeRate = Math.min(defender.getStats().getDodgeRate() != null ? 
            defender.getStats().getDodgeRate() : 0.0, 0.8);
        if (Math.random() < dodgeRate) {
            result.setIsDodged(true);
            result.setValue(0);
            result.setIsCritical(false);
            result.setIsHeal(false);
            return result;
        }

        // 2. 基础伤害计算
        int attack = attacker.getStats().getPhysicalAttack();
        int defense = defender.getStats().getDefense();
        double powerMultiplier = skillPower / 100.0;
        
        double baseDamage = attack * powerMultiplier - defense * 0.5;
        
        // 3. 暴击判定
        double criticalRate = Math.min(attacker.getStats().getCriticalRate() != null ? 
            attacker.getStats().getCriticalRate() : 0.0, 0.8);
        boolean isCritical = Math.random() < criticalRate;
        
        if (isCritical) {
            baseDamage *= 1.5;
        }
        
        // 4. 随机波动 (±10%)
        double randomFactor = 1 + (Math.random() * 0.2 - 0.1);
        double finalDamage = Math.max(1, Math.floor(baseDamage * randomFactor));
        
        result.setValue((int) finalDamage);
        result.setIsCritical(isCritical);
        result.setIsDodged(false);
        result.setIsHeal(false);
        
        return result;
    }

    /**
     * 计算治疗量
     * @param caster 施法者
     * @param target 目标
     * @param skillPower 技能威力倍率
     * @return 治疗量
     */
    public static int calculateHeal(CombatantVO caster, CombatantVO target, int skillPower) {
        int magicAttack = caster.getStats().getMagicAttack();
        double powerMultiplier = skillPower / 100.0;
        
        int healAmount = (int) Math.floor(magicAttack * powerMultiplier);
        int maxHeal = target.getStats().getMaxHp() - target.getStats().getHp();
        
        return Math.min(healAmount, maxHeal);
    }

    /**
     * 应用Buff
     * @param target 目标
     * @param buff Buff效果
     */
    public static void applyBuff(CombatantVO target, BuffEffectVO buff) {
        if (target.getBuffs() == null) {
            target.setBuffs(new ArrayList<>());
        }
        
        // 同名Buff不叠加，后施加的覆盖先施加的
        target.getBuffs().removeIf(b -> b.getName().equals(buff.getName()));
        target.getBuffs().add(buff);
    }

    /**
     * Buff结算（每回合开始时调用）
     * @param combatant 参战单位
     * @return 结算结果列表
     */
    public static List<BattleStateVO.BuffSettlementResultVO> settleBuffs(CombatantVO combatant) {
        List<BattleStateVO.BuffSettlementResultVO> results = new ArrayList<>();
        
        if (combatant.getBuffs() == null || combatant.getBuffs().isEmpty()) {
            return results;
        }
        
        Iterator<BuffEffectVO> iterator = combatant.getBuffs().iterator();
        while (iterator.hasNext()) {
            BuffEffectVO buff = iterator.next();
            
            BattleStateVO.BuffSettlementResultVO result = new BattleStateVO.BuffSettlementResultVO();
            result.setTargetUid(combatant.getUid());
            result.setBuffName(buff.getName());
            result.setIsDebuff(buff.getIsDebuff());
            result.setValue(buff.getValue());
            
            // 减少持续回合
            buff.setRemainingTurns(buff.getRemainingTurns() - 1);
            result.setRemainingTurns(buff.getRemainingTurns());
            
            // 处理DoT伤害
            if (buff.getIsDebuff() && "maxHp".equals(buff.getStat())) {
                int dotDamage = Math.max(1, (int) Math.floor(combatant.getStats().getMaxHp() * 0.1));
                combatant.getStats().setHp(Math.max(0, combatant.getStats().getHp() - dotDamage));
                result.setValue((double) dotDamage);
            }
            
            // Buff到期移除
            if (buff.getRemainingTurns() <= 0) {
                iterator.remove();
                result.setExpired(true);
            } else {
                result.setExpired(false);
            }
            
            results.add(result);
        }
        
        return results;
    }

    /**
     * 获取应用Buff后的实际属性
     * @param combatant 参战单位
     * @param stat 属性名
     * @return 实际属性值
     */
    public static double getActualStat(CombatantVO combatant, String stat) {
        double baseValue = getBaseStat(combatant, stat);
        
        if (combatant.getBuffs() != null) {
            for (BuffEffectVO buff : combatant.getBuffs()) {
                if (stat.equals(buff.getStat())) {
                    baseValue += buff.getIsDebuff() ? -buff.getValue() : buff.getValue();
                }
            }
        }
        
        return Math.max(0, baseValue);
    }

    /**
     * 获取基础属性值
     */
    private static double getBaseStat(CombatantVO combatant, String stat) {
        switch (stat) {
            case "physicalAttack": return combatant.getStats().getPhysicalAttack();
            case "magicAttack": return combatant.getStats().getMagicAttack();
            case "defense": return combatant.getStats().getDefense();
            case "speed": return combatant.getStats().getSpeed();
            case "dodgeRate": return combatant.getStats().getDodgeRate() != null ? combatant.getStats().getDodgeRate() : 0.0;
            case "criticalRate": return combatant.getStats().getCriticalRate() != null ? combatant.getStats().getCriticalRate() : 0.0;
            case "maxHp": return combatant.getStats().getMaxHp();
            default: return 0;
        }
    }

    /**
     * 计算逃跑成功率
     * @param speed 速度
     * @return 逃跑成功率(0~1)
     */
    public static double calculateFleeChance(int speed) {
        return Math.min(0.3 + speed * 0.01, 0.8);
    }

    /**
     * 检查战斗是否结束
     * @param combatants 所有参战单位
     * @return 战斗结果 (null表示未结束, "victory"表示胜利, "defeat"表示失败)
     */
    public static String checkBattleEnd(List<CombatantVO> combatants) {
        boolean hasAliveAlly = false;
        boolean hasAliveEnemy = false;
        
        for (CombatantVO combatant : combatants) {
            if (combatant.getIsAlive()) {
                if ("ally".equals(combatant.getSide())) {
                    hasAliveAlly = true;
                } else if ("enemy".equals(combatant.getSide())) {
                    hasAliveEnemy = true;
                }
            }
        }
        
        if (!hasAliveEnemy) {
            return "victory";
        }
        if (!hasAliveAlly) {
            return "defeat";
        }
        
        return null;
    }

    /**
     * 伤害结果内部类
     */
    public static class DamageResult {
        private String targetUid;
        private Integer value;
        private Boolean isCritical;
        private Boolean isDodged;
        private Boolean isHeal;
        private String sourceType;

        public String getTargetUid() { return targetUid; }
        public void setTargetUid(String targetUid) { this.targetUid = targetUid; }
        public Integer getValue() { return value; }
        public void setValue(Integer value) { this.value = value; }
        public Boolean getIsCritical() { return isCritical; }
        public void setIsCritical(Boolean critical) { isCritical = critical; }
        public Boolean getIsDodged() { return isDodged; }
        public void setIsDodged(Boolean dodged) { isDodged = dodged; }
        public Boolean getIsHeal() { return isHeal; }
        public void setIsHeal(Boolean heal) { isHeal = heal; }
        public String getSourceType() { return sourceType; }
        public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    }
}
