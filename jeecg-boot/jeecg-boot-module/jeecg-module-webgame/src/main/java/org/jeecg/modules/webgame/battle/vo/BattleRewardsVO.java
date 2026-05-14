package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

/**
 * @Description: 战斗奖励VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class BattleRewardsVO {

    /** 获得经验 */
    private Integer exp;

    /** 获得金币 */
    private Integer gold;

    /** 获得物品列表 */
    private java.util.List<BattleRewardItemVO> items;

    /** 战宠获得经验 */
    private Integer petExp;

    /** 是否升级 */
    private Boolean levelUp;

    /** 升级后等级 */
    private Integer newLevel;
}
