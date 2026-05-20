package org.jeecg.modules.webgame.pvp.service;

import org.jeecg.modules.webgame.pvp.dto.PvpMatchRequestDTO;
import org.jeecg.modules.webgame.pvp.vo.ArenaPlayerVO;
import org.jeecg.modules.webgame.pvp.vo.ArenaSeasonVO;
import org.jeecg.modules.webgame.pvp.vo.PvpMatchOpponentVO;
import org.jeecg.modules.webgame.pvp.vo.PvpSettlementResultVO;

/**
 * @Description: 竞技场服务接口
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
public interface IArenaService {

    /**
     * 获取当前活跃赛季信息
     * @return 赛季信息
     */
    ArenaSeasonVO getCurrentSeason();

    /**
     * 获取玩家在当前赛季的竞技数据
     * @param characterId 角色ID
     * @return 玩家竞技数据
     */
    ArenaPlayerVO getPlayerArenaData(String characterId);

    /**
     * PVP匹配对手
     * @param playerCharacterId 玩家角色ID
     * @param request 匹配请求
     * @return 对手信息
     */
    PvpMatchOpponentVO matchOpponent(String playerCharacterId, PvpMatchRequestDTO request);

    /**
     * 结算PVP战斗结果
     * @param playerCharacterId 玩家角色ID
     * @param opponentCharacterId 对手角色ID
     * @param won 是否胜利
     * @return 结算结果
     */
    PvpSettlementResultVO settleBattle(String playerCharacterId, String opponentCharacterId, boolean won);
}
