package org.jeecg.modules.webgame.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.social.dto.SendMessageDTO;
import org.jeecg.modules.webgame.social.entity.WgChatConversation;
import org.jeecg.modules.webgame.social.entity.WgChatMessage;
import org.jeecg.modules.webgame.social.entity.WgChatUnread;
import org.jeecg.modules.webgame.social.mapper.WgChatConversationMapper;
import org.jeecg.modules.webgame.social.mapper.WgChatMessageMapper;
import org.jeecg.modules.webgame.social.mapper.WgChatUnreadMapper;
import org.jeecg.modules.webgame.social.service.IChatService;
import org.jeecg.modules.webgame.social.vo.ChatMessageVO;
import org.jeecg.modules.webgame.social.vo.PrivateConversationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 聊天系统 Service 实现
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@Service
public class ChatServiceImpl implements IChatService {
    
    @Autowired
    private WgChatMessageMapper chatMessageMapper;
    
    @Autowired
    private WgChatConversationMapper conversationMapper;
    
    @Autowired
    private WgChatUnreadMapper unreadMapper;
    
    @Autowired
    private WgCharacterMapper characterMapper;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final int MAX_WORLD_MESSAGES = 200;
    private static final int MAX_PRIVATE_MESSAGES = 500;
    
    @Override
    public List<ChatMessageVO> getWorldMessages(Integer limit) {
        log.info("获取世界频道消息, limit: {}", limit);
        
        if (limit == null || limit <= 0) {
            limit = 100;
        }
        if (limit > 200) {
            limit = 200;
        }
        
        List<WgChatMessage> messages = chatMessageMapper.selectWorldMessages(limit);
        return messages.stream()
            .map(this::buildChatMessageVO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PrivateConversationVO> getConversations(String characterId) {
        log.info("获取私聊会话列表, characterId: {}", characterId);
        
        List<WgChatConversation> conversations = conversationMapper.selectConversationsByCharacter(characterId);
        
        return conversations.stream()
            .map(conv -> buildPrivateConversationVO(conv, characterId))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ChatMessageVO> getPrivateMessages(String characterId, String targetId, Integer limit) {
        log.info("获取私聊消息记录, characterId: {}, targetId: {}, limit: {}", characterId, targetId, limit);
        
        if (limit == null || limit <= 0) {
            limit = 50;
        }
        
        // 确保 user1Id < user2Id
        String user1Id = characterId;
        String user2Id = targetId;
        if (user1Id.compareTo(user2Id) > 0) {
            String temp = user1Id;
            user1Id = user2Id;
            user2Id = temp;
        }
        
        List<WgChatMessage> messages = chatMessageMapper.selectPrivateMessages(user1Id, user2Id, limit);
        
        // 反转列表，使消息按时间正序排列
        Collections.reverse(messages);
        
        return messages.stream()
            .map(this::buildChatMessageVO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMessageVO sendMessage(String senderId, SendMessageDTO dto) {
        log.info("发送消息, senderId: {}, channel: {}", senderId, dto.getChannel());
        
        // 1. 校验参数
        if (!"world".equals(dto.getChannel()) && !"private".equals(dto.getChannel())) {
            throw new JeecgBootException("无效的频道类型");
        }
        
        if ("private".equals(dto.getChannel()) && (dto.getTargetId() == null || dto.getTargetId().isEmpty())) {
            throw new JeecgBootException("私聊必须指定目标角色");
        }
        
        // 2. 敏感词过滤（简单实现，实际应使用敏感词库）
        String content = filterSensitiveWords(dto.getContent());
        
        // 3. 创建消息
        WgChatMessage message = new WgChatMessage();
        message.setId(UUID.randomUUID().toString());
        message.setSenderId(senderId);
        message.setChannel(dto.getChannel());
        message.setTargetId(dto.getTargetId());
        message.setContent(content);
        message.setCreatedAt(new Date());
        
        chatMessageMapper.insert(message);
        
        // 4. 如果是私聊，更新会话和未读数
        if ("private".equals(dto.getChannel())) {
            updateConversationAndUnread(senderId, dto.getTargetId(), content);
        } else {
            // 世界频道消息清理（保留最近200条）
            cleanupWorldMessages();
        }
        
        // 5. 返回消息VO
        return buildChatMessageVO(message);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(String characterId, String targetId) {
        log.info("标记会话为已读, characterId: {}, targetId: {}", characterId, targetId);
        
        // 确定会话ID
        String user1Id = characterId;
        String user2Id = targetId;
        if (user1Id.compareTo(user2Id) > 0) {
            String temp = user1Id;
            user1Id = user2Id;
            user2Id = temp;
        }
        
        LambdaQueryWrapper<WgChatConversation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgChatConversation::getUser1Id, user1Id)
               .eq(WgChatConversation::getUser2Id, user2Id);
        
        WgChatConversation conversation = conversationMapper.selectOne(wrapper);
        if (conversation == null) {
            return;
        }
        
        // 清零未读数
        LambdaQueryWrapper<WgChatUnread> unreadWrapper = new LambdaQueryWrapper<>();
        unreadWrapper.eq(WgChatUnread::getConversationId, conversation.getId())
                     .eq(WgChatUnread::getCharacterId, characterId);
        
        WgChatUnread unread = unreadMapper.selectOne(unreadWrapper);
        if (unread != null) {
            unread.setUnreadCount(0);
            unreadMapper.updateById(unread);
        }
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 构建聊天消息 VO
     */
    private ChatMessageVO buildChatMessageVO(WgChatMessage message) {
        ChatMessageVO vo = new ChatMessageVO();
        vo.setId(message.getId());
        vo.setSenderId(message.getSenderId());
        vo.setChannel(message.getChannel());
        vo.setTargetId(message.getTargetId());
        vo.setContent(message.getContent());
        vo.setTimestamp(sdf.format(message.getCreatedAt()));
        
        // 填充发送者信息
        WgCharacter sender = characterMapper.selectById(message.getSenderId());
        if (sender != null) {
            vo.setSenderName(sender.getCharacterName());
            vo.setSenderProfession(getProfessionName(sender.getProfession()));
        }
        
        // 如果是私聊，填充目标名称
        if ("private".equals(message.getChannel()) && message.getTargetId() != null) {
            WgCharacter target = characterMapper.selectById(message.getTargetId());
            if (target != null) {
                vo.setTargetName(target.getCharacterName());
            }
        }
        
        return vo;
    }
    
    /**
     * 构建私聊会话 VO
     */
    private PrivateConversationVO buildPrivateConversationVO(WgChatConversation conv, String characterId) {
        PrivateConversationVO vo = new PrivateConversationVO();
        
        // 确定对方角色ID
        String targetId = conv.getUser1Id().equals(characterId) ? conv.getUser2Id() : conv.getUser1Id();
        vo.setTargetId(targetId);
        
        // 填充对方信息
        WgCharacter target = characterMapper.selectById(targetId);
        if (target == null) {
            return null;
        }
        vo.setTargetName(target.getCharacterName());
        vo.setTargetProfession(getProfessionName(target.getProfession()));
        
        vo.setLastMessage(conv.getLastMessage());
        vo.setLastTime(sdf.format(conv.getLastTime()));
        
        // 查询未读数
        WgChatUnread unread = unreadMapper.selectByConversationAndCharacter(conv.getId(), characterId);
        vo.setUnreadCount(unread != null ? unread.getUnreadCount() : 0);
        
        return vo;
    }
    
    /**
     * 更新会话和未读数
     */
    private void updateConversationAndUnread(String senderId, String targetId, String content) {
        // 确定 user1Id < user2Id
        String user1Id = senderId;
        String user2Id = targetId;
        if (user1Id.compareTo(user2Id) > 0) {
            String temp = user1Id;
            user1Id = user2Id;
            user2Id = temp;
        }
        
        // 查询或创建会话
        LambdaQueryWrapper<WgChatConversation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgChatConversation::getUser1Id, user1Id)
               .eq(WgChatConversation::getUser2Id, user2Id);
        
        WgChatConversation conversation = conversationMapper.selectOne(wrapper);
        if (conversation == null) {
            conversation = new WgChatConversation();
            conversation.setId(UUID.randomUUID().toString());
            conversation.setUser1Id(user1Id);
            conversation.setUser2Id(user2Id);
            conversation.setLastTime(new Date());
            conversationMapper.insert(conversation);
        }
        
        // 更新会话最后消息
        conversation.setLastMessage(content);
        conversation.setLastTime(new Date());
        conversation.setUpdatedAt(new Date());
        conversationMapper.updateById(conversation);
        
        // 增加接收者的未读数
        increaseUnreadCount(conversation.getId(), targetId);
    }
    
    /**
     * 增加未读数
     */
    private void increaseUnreadCount(String conversationId, String characterId) {
        WgChatUnread unread = unreadMapper.selectByConversationAndCharacter(conversationId, characterId);
        if (unread == null) {
            unread = new WgChatUnread();
            unread.setId(UUID.randomUUID().toString());
            unread.setConversationId(conversationId);
            unread.setCharacterId(characterId);
            unread.setUnreadCount(1);
            unreadMapper.insert(unread);
        } else {
            unread.setUnreadCount(unread.getUnreadCount() + 1);
            unreadMapper.updateById(unread);
        }
    }
    
    /**
     * 清理世界频道消息（保留最近200条）
     */
    private void cleanupWorldMessages() {
        LambdaQueryWrapper<WgChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgChatMessage::getChannel, "world")
               .orderByDesc(WgChatMessage::getCreatedAt)
               .last("LIMIT 1 OFFSET " + MAX_WORLD_MESSAGES);
        
        List<WgChatMessage> oldMessages = chatMessageMapper.selectList(wrapper);
        if (!oldMessages.isEmpty()) {
            // 删除超出限制的消息
            LambdaQueryWrapper<WgChatMessage> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(WgChatMessage::getChannel, "world")
                        .lt(WgChatMessage::getCreatedAt, oldMessages.get(0).getCreatedAt());
            chatMessageMapper.delete(deleteWrapper);
        }
    }
    
    /**
     * 敏感词过滤（简单实现）
     */
    private String filterSensitiveWords(String content) {
        // TODO: 实际应使用敏感词库进行过滤
        // 这里只是示例，替换一些简单的敏感词
        String[] sensitiveWords = {"敏感词1", "敏感词2"};
        for (String word : sensitiveWords) {
            content = content.replace(word, "***");
        }
        return content;
    }
    
    /**
     * 获取职业名称
     */
    private String getProfessionName(Integer profession) {
        if (profession == null) {
            return "Unknown";
        }
        switch (profession) {
            case 1: return "Warrior";
            case 2: return "Mage";
            case 3: return "Hunter";
            default: return "Unknown";
        }
    }
}
