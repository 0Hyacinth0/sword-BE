package org.jeecg.modules.webgame.social.service;

import org.jeecg.modules.webgame.social.dto.SendMessageDTO;
import org.jeecg.modules.webgame.social.vo.ChatMessageVO;
import org.jeecg.modules.webgame.social.vo.PrivateConversationVO;

import java.util.List;

/**
 * @Description: 聊天系统 Service
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
public interface IChatService {
    
    /**
     * 获取世界频道消息
     * @param limit 消息条数
     * @return 消息列表
     */
    List<ChatMessageVO> getWorldMessages(Integer limit);
    
    /**
     * 获取私聊会话列表
     * @param characterId 角色ID
     * @return 会话列表
     */
    List<PrivateConversationVO> getConversations(String characterId);
    
    /**
     * 获取私聊消息记录
     * @param characterId 当前角色ID
     * @param targetId 目标角色ID
     * @param limit 消息条数
     * @return 消息列表
     */
    List<ChatMessageVO> getPrivateMessages(String characterId, String targetId, Integer limit);
    
    /**
     * 发送消息
     * @param senderId 发送者角色ID
     * @param dto 消息内容
     * @return 发送的消息
     */
    ChatMessageVO sendMessage(String senderId, SendMessageDTO dto);
    
    /**
     * 标记会话为已读
     * @param characterId 角色ID
     * @param targetId 目标角色ID
     */
    void markAsRead(String characterId, String targetId);
}
