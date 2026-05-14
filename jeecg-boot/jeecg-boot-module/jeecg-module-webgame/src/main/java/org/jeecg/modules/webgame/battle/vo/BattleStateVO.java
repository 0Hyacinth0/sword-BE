package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description: 战斗状态VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class BattleStateVO {

    /** 战斗ID */
    private String battleId;

    /** 当前阶段 */
    private String phase;

    /** 当前回合 */
    private Integer round;

    /** 参战单位列表 */
    private List<CombatantVO> combatants;

    /** 行动顺序预览 */
    private List<ActionOrderEntryVO> actionOrder;

    /** 当前行动者索引 */
    private Integer currentActorIndex;

    /** 是否等待玩家行动 */
    private Boolean waitingForPlayerAction;

    /** 战斗日志 */
    private List<BattleLogEntryVO> log;

    /** 战斗结果 */
    private String outcome;

    /** 战斗奖励 */
    private BattleRewardsVO rewards;

    /** 最后伤害结果（用于动画） */
    private List<DamageResultVO> lastDamageResults;

    /** 最后Buff结算结果（用于动画） */
    private List<BuffSettlementResultVO> lastBuffResults;

    /** 行动顺序条目VO */
    @Data
    public static class ActionOrderEntryVO {
        private String uid;
        private String name;
        private String side;
        private String type;
        private Integer actionValue;
        private Double actionValuePercent;
        private Boolean isAlive;
    }

    /** 伤害结果VO */
    @Data
    public static class DamageResultVO {
        private String targetUid;
        private Integer value;
        private Boolean isCritical;
        private Boolean isDodged;
        private Boolean isHeal;
        private String sourceType; // attack/skill/item/buff
    }

    /** Buff结算结果VO */
    @Data
    public static class BuffSettlementResultVO {
        private String targetUid;
        private String buffName;
        private Boolean isDebuff;
        private Double value;
        private Integer remainingTurns;
        private Boolean expired;
    }
}
