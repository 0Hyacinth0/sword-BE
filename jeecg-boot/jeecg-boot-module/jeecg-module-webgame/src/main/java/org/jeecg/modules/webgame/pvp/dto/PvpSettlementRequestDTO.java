package org.jeecg.modules.webgame.pvp.dto;

import lombok.Data;

/**
 * @Description: PVP战斗结算请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class PvpSettlementRequestDTO {

    /** 玩家角色ID */
    private String playerCharacterId;

    /** 对手角色ID */
    private String opponentCharacterId;

    /** 是否胜利 */
    private Boolean won;
}
