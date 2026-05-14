package org.jeecg.modules.webgame.battle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description: 发起战斗请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class StartBattleDTO {

    @NotBlank(message = "角色ID不能为空")
    private String characterId;

    private String dungeonId;

    private String stageId;

    private String enemyGroupId;

    private String activePetId;
}
