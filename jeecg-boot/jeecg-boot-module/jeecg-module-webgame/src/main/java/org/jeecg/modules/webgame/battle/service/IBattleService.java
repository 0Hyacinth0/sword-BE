package org.jeecg.modules.webgame.battle.service;

import org.jeecg.modules.webgame.battle.dto.BattleActionDTO;
import org.jeecg.modules.webgame.battle.dto.StartBattleDTO;
import org.jeecg.modules.webgame.battle.vo.*;
import org.jeecg.modules.webgame.battle.vo.BattleStateVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 战斗服务接口
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
public interface IBattleService {

    /**
     * 发起战斗
     * @param dto 发起战斗参数
     * @return 战斗状态
     */
    BattleStateVO startBattle(StartBattleDTO dto);

    /**
     * 提交行动
     * @param dto 行动参数
     * @return 更新后的战斗状态
     */
    BattleStateVO submitAction(BattleActionDTO dto);

    /**
     * 结束战斗并领取奖励
     * @param battleId 战斗ID
     * @return 战斗结果
     */
    BattleEndVO endBattle(String battleId);

    /**
     * 获取角色技能列表
     * @param characterId 角色ID
     * @return 技能列表
     */
    SkillListVO getCharacterSkills(String characterId);

    /**
     * 查询战斗状态（用于断线重连）
     * @param battleId 战斗ID
     * @return 战斗状态
     */
    BattleStateVO getBattleState(String battleId);
}
