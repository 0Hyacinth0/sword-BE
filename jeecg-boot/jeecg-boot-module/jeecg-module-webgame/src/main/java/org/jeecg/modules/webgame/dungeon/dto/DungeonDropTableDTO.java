package org.jeecg.modules.webgame.dungeon.dto;

import lombok.Data;
import org.jeecg.modules.webgame.battle.vo.BattleRewardItemVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 副本掉落表DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
public class DungeonDropTableDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**副本ID*/
    private String dungeonId;
    
    /**掉落率倍率*/
    private BigDecimal dropRateMultiplier;
    
    /**普通层掉落列表*/
    private List<BattleRewardItemVO> floorDrops;
    
    /**Boss层掉落列表*/
    private List<BattleRewardItemVO> bossDrops;
}
