package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

/**
 * @Description: 战斗结束响应VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class BattleEndVO {

    /** 战斗结果 */
    private String outcome; // victory/defeat/fled

    /** 战斗奖励 */
    private BattleRewardsVO rewards;

    /** 战斗统计 */
    private BattleStatisticsVO statistics;

    /** 战宠HP已恢复 */
    private Boolean petHpRestored;

    /** 战斗统计VO */
    @Data
    public static class BattleStatisticsVO {
        /** 战斗总回合数 */
        private Integer totalRounds;

        /** 玩家造成的总伤害 */
        private Integer totalDamageDealt;

        /** 玩家承受的总伤害 */
        private Integer totalDamageTaken;

        /** 玩家治疗总量 */
        private Integer totalHealed;

        /** 暴击次数 */
        private Integer criticalHits;

        /** 闪避次数 */
        private Integer dodgeCount;

        /** 击杀敌人数 */
        private Integer enemiesKilled;
    }
}
