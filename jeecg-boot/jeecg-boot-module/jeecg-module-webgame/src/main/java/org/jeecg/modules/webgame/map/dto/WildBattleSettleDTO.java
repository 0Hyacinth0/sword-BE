package org.jeecg.modules.webgame.map.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: 野外战斗结算请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
public class WildBattleSettleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotBlank(message = "角色ID不能为空")
    private String characterId;
    
    /**区域ID*/
    @NotBlank(message = "区域ID不能为空")
    private String areaId;
    
    /**怪物ID*/
    @NotBlank(message = "怪物ID不能为空")
    private String monsterId;
    
    /**战斗结果（win/lose）*/
    @NotBlank(message = "战斗结果不能为空")
    private String battleResult;
    
    /**战斗回合数*/
    @NotNull(message = "战斗回合数不能为空")
    private Integer rounds;
    
    /**战斗日志（可选，用于反作弊校验）*/
    private String battleLog;
}
