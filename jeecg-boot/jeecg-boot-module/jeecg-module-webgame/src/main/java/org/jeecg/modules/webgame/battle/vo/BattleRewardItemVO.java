package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

/**
 * @Description: 战斗奖励物品VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class BattleRewardItemVO {

    /** 物品ID */
    private String itemId;

    /** 物品名称 */
    private String name;

    /** 物品数量 */
    private Integer quantity;

    /** 物品品质 */
    private String quality; // common/rare/epic/legendary

    /** 物品类型 */
    private String itemType; // equipment/material/consumable/pet_egg
}
