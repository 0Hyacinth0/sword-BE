package org.jeecg.modules.webgame.social.service;

import org.jeecg.modules.webgame.social.vo.TeamInfoVO;

/**
 * @Description: 多人副本大厅 Service
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
public interface IDungeonRoomService {
    
    /**
     * 创建副本房间
     * @param leaderId 队长角色ID
     * @param teamId 队伍ID
     * @param dungeonId 副本ID
     * @return 房间信息
     */
    TeamInfoVO createDungeonRoom(String leaderId, String teamId, String dungeonId);
    
    /**
     * 获取房间信息
     * @param roomId 房间ID
     * @return 房间信息
     */
    TeamInfoVO getDungeonRoom(String roomId);
    
    /**
     * 切换准备状态
     * @param characterId 角色ID
     * @param roomId 房间ID
     * @return 更新后的房间信息
     */
    TeamInfoVO toggleReady(String characterId, String roomId);
    
    /**
     * 开始挑战
     * @param leaderId 队长角色ID
     * @param roomId 房间ID
     * @return 战斗初始化数据
     */
    Object startChallenge(String leaderId, String roomId);
    
    /**
     * 离开房间
     * @param characterId 角色ID
     * @param roomId 房间ID
     */
    void leaveRoom(String characterId, String roomId);
    
    /**
     * 取消房间
     * @param leaderId 队长角色ID
     * @param roomId 房间ID
     */
    void cancelRoom(String leaderId, String roomId);
}
