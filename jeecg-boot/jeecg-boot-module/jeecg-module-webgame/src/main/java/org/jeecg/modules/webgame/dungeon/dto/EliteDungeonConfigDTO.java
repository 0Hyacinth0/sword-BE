package org.jeecg.modules.webgame.dungeon.dto;

import lombok.Data;
import org.jeecg.modules.webgame.battle.vo.BattleSkillVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 精英副本配置DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
public class EliteDungeonConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**副本ID*/
    private String dungeonId;
    
    /**是否为精英副本*/
    private Boolean isElite;
    
    /**属性倍率*/
    private BigDecimal statMultiplier;
    
    /**技能组ID*/
    private Integer skillGroupId;
    
    /**技能列表*/
    private List<BattleSkillVO> skills;
}
