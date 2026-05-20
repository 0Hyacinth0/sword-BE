package org.jeecg.modules.webgame.pvp.dto;

import lombok.Data;

/**
 * @Description: PVP匹配请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class PvpMatchRequestDTO {

    /** 玩家当前积分 */
    private Integer score;
}
