package org.jeecg.modules.webgame.map.vo;

import lombok.Data;
import org.jeecg.modules.webgame.battle.vo.BattleRewardItemVO;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 野外战斗结算结果VO
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
public class WildBattleSettleResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**获得的经验值*/
    private Integer exp;
    
    /**获得的金币*/
    private Integer gold;
    
    /**获得的物品列表*/
    private List<BattleRewardItemVO> items;
    
    /**是否升级*/
    private Boolean levelUp;
    
    /**战宠获得的经验值*/
    private Integer petExp;
}
