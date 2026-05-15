package org.jeecg.modules.webgame.social.service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 多人副本战斗 Service
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
public interface IMultiBattleService {
    
    /**
     * 初始化多人战斗
     * @param roomId 房间ID
     * @param dungeonId 副本ID
     * @return 战斗初始化数据
     */
    Map<String, Object> initMultiBattle(String roomId, String dungeonId);
    
    /**
     * 楼层结算
     * @param battleId 战斗ID
     * @param floor 楼层
     * @param isBossFloor 是否Boss层
     * @param memberIds 成员ID列表
     * @return 掉落分配结果
     */
    Map<String, Object> floorComplete(String battleId, Integer floor, boolean isBossFloor, List<String> memberIds);
    
    /**
     * 副本通关结算
     * @param battleId 战斗ID
     * @param memberIds 成员ID列表
     * @return 通关奖励
     */
    Map<String, Object> dungeonComplete(String battleId, List<String> memberIds);
    
    /**
     * 计算独立掉落
     * @param characterId 角色ID
     * @param dungeonId 副本ID
     * @param floor 楼层
     * @param dropRateMultiplier 掉落率倍率
     * @return 掉落物品列表
     */
    List<Map<String, Object>> calculateDrops(String characterId, String dungeonId, Integer floor, double dropRateMultiplier);
}
