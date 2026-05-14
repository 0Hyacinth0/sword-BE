package org.jeecg.modules.webgame.map.service;

import org.jeecg.modules.webgame.map.dto.WildBattleSettleDTO;
import org.jeecg.modules.webgame.map.vo.WildBattleSettleResultVO;

/**
 * @Description: 野外战斗Service
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
public interface IWildBattleService {
    
    /**
     * 野外战斗结算（验证模式）
     * @param settleDTO 结算请求
     * @return 结算结果
     */
    WildBattleSettleResultVO settleWildBattle(WildBattleSettleDTO settleDTO);
}
