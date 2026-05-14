package org.jeecg.modules.webgame.dungeon.service;

import org.jeecg.modules.webgame.battle.vo.BattleRewardItemVO;
import org.jeecg.modules.webgame.dungeon.dto.DungeonDropTableDTO;

import java.util.List;

/**
 * @Description: 副本掉落Service
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
public interface IDungeonDropService {
    
    /**
     * 获取副本掉落表配置
     * @param dungeonId 副本ID
     * @return 掉落表配置
     */
    DungeonDropTableDTO getDropTable(String dungeonId);
    
    /**
     * 解析掉落（模拟掷骰）
     * @param dungeonId 副本ID
     * @param isBossFloor 是否为Boss层
     * @return 掉落物品列表
     */
    List<BattleRewardItemVO> resolveDrops(String dungeonId, boolean isBossFloor);
}
