package org.jeecg.modules.webgame.battle.dto;

import lombok.Data;

/**
 * @Description: 战斗行动请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class BattleActionDTO {

    /** 战斗ID */
    private String battleId;

    /** 行动类型: attack/skill/item/defend/flee */
    private String type;

    /** 行动者UID */
    private String actorUid;

    /** 目标UID */
    private String targetUid;

    /** 技能ID */
    private Integer skillId;

    /** 物品ID */
    private String itemId;
}
