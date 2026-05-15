package org.jeecg.modules.webgame.social.service;

import org.jeecg.modules.webgame.social.dto.FriendRequestDTO;
import org.jeecg.modules.webgame.social.dto.HandleFriendRequestDTO;
import org.jeecg.modules.webgame.social.vo.FriendInfoVO;
import org.jeecg.modules.webgame.social.vo.FriendListVO;
import org.jeecg.modules.webgame.social.vo.SearchPlayerVO;

import java.util.List;

/**
 * @Description: 好友系统 Service
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
public interface IFriendService {
    
    /**
     * 获取好友列表
     * @param characterId 角色ID
     * @return 好友列表数据
     */
    FriendListVO getFriendList(String characterId);
    
    /**
     * 搜索玩家
     * @param keyword 搜索关键词
     * @param characterId 当前角色ID
     * @return 搜索结果列表
     */
    List<SearchPlayerVO> searchPlayers(String keyword, String characterId);
    
    /**
     * 发送好友请求
     * @param fromCharacterId 发送者角色ID
     * @param dto 请求参数
     * @return 请求ID
     */
    String sendFriendRequest(String fromCharacterId, FriendRequestDTO dto);
    
    /**
     * 接受好友请求
     * @param characterId 接收者角色ID
     * @param dto 请求参数
     * @return 好友信息
     */
    FriendInfoVO acceptFriendRequest(String characterId, HandleFriendRequestDTO dto);
    
    /**
     * 拒绝好友请求
     * @param characterId 接收者角色ID
     * @param dto 请求参数
     */
    void rejectFriendRequest(String characterId, HandleFriendRequestDTO dto);
    
    /**
     * 删除好友
     * @param characterId 当前角色ID
     * @param friendCharacterId 好友角色ID
     */
    void removeFriend(String characterId, String friendCharacterId);
    
    /**
     * 取消好友请求
     * @param characterId 发送者角色ID
     * @param requestId 请求ID
     */
    void cancelFriendRequest(String characterId, String requestId);
}
