package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

/**
 * @Description: 战斗技能VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class BattleSkillVO {

    private Integer id;

    private String name;

    private String type; // active_attack/active_heal/active_buff/passive

    private Integer power;

    private Integer cooldown;

    private Integer mpCost;

    private String targetType; // single_enemy/all_enemies/self/single_ally/all_allies

    private String description;

    private Integer element;
}
