package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

/**
 * @Description: 战斗参战单位VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class CombatantVO {

    /** 唯一ID（战斗实例内的临时标识） */
    private String uid;

    /** 原始ID（角色/战宠/怪物ID） */
    private String sourceId;

    /** 显示名称 */
    private String name;

    /** 阵营：ally/enemy */
    private String side;

    /** 类型：player/pet/enemy */
    private String type;

    /** 战斗属性 */
    private CombatantStatsVO stats;

    /** 已学会的技能列表 */
    private java.util.List<BattleSkillVO> skills;

    /** 当前Buff/Debuff列表 */
    private java.util.List<BuffEffectVO> buffs;

    /** 技能冷却 { skillId: remainingTurns } */
    private java.util.Map<Integer, Integer> cooldowns;

    /** 是否存活 */
    private Boolean isAlive;

    /** 行动值（ATB系统累积量） */
    private Integer actionValue;

    /** 主人uid（仅type=pet时有值） */
    private String masterUid;

    /** 战宠给予主人的属性加成（仅type=pet时有值） */
    private PetBonusToMasterVO petBonusToMaster;

    /** 等级 */
    private Integer level;

    /** 战斗属性VO */
    @Data
    public static class CombatantStatsVO {
        private Integer maxHp;
        private Integer hp;
        private Integer maxMp;
        private Integer mp;
        private Integer physicalAttack;
        private Integer magicAttack;
        private Integer defense;
        private Integer speed;
        private Double dodgeRate;
        private Double criticalRate;
    }
}
