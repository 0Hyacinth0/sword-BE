package org.jeecg.modules.webgame.social.service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 团队副本Boss战 Service
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
public interface IBossBattleService {
    
    /**
     * 初始化Boss战斗
     * @param battleId 战斗实例ID
     * @param bossId Boss ID
     * @return Boss战斗数据
     */
    Map<String, Object> initBossBattle(String battleId, String bossId);
    
    /**
     * 切换Boss阶段
     * @param battleId 战斗实例ID
     * @param newPhase 新阶段
     * @return 更新后的Boss状态
     */
    Map<String, Object> switchPhase(String battleId, Integer newPhase);
    
    /**
     * 触发狂暴
     * @param battleId 战斗实例ID
     * @return 狂暴效果
     */
    Map<String, Object> triggerEnrage(String battleId);
    
    /**
     * 复活队友
     * @param battleId 战斗实例ID
     * @param reviverId 复活者ID
     * @param revivedId 被复活者ID
     * @return 复活结果
     */
    Map<String, Object> reviveTeammate(String battleId, String reviverId, String revivedId);
    
    /**
     * 使用全屏AOE技能
     * @param battleId 战斗实例ID
     * @param skillId 技能ID
     * @return AOE伤害结果
     */
    Map<String, Object> useAoeSkill(String battleId, String skillId);
}
