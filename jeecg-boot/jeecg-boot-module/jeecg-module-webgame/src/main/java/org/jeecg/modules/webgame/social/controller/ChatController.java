package org.jeecg.modules.webgame.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.common.controller.BaseWebGameController;
import org.jeecg.modules.webgame.social.dto.SendMessageDTO;
import org.jeecg.modules.webgame.social.service.IChatService;
import org.jeecg.modules.webgame.social.vo.ChatMessageVO;
import org.jeecg.modules.webgame.social.vo.PrivateConversationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 聊天系统 Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@RestController
@RequestMapping("/webgame/chat")
public class ChatController extends BaseWebGameController {
    
    @Autowired
    private IChatService chatService;
    
    @Autowired
    private WgCharacterMapper characterMapper;
    
    /**
     * 获取世界频道消息
     */
    @GetMapping("/world")
    public Result<List<ChatMessageVO>> getWorldMessages(
            @RequestParam(required = false, defaultValue = "100") Integer limit) {
        log.info("获取世界频道消息, limit: {}", limit);
        
        List<ChatMessageVO> messages = chatService.getWorldMessages(limit);
        return Result.OK("获取成功", messages);
    }
    
    /**
     * 获取私聊会话列表（兼容接口，自动获取当前用户的第一个角色）
     */
    @GetMapping("/conversations")
    public Result<List<PrivateConversationVO>> getConversationsAuto(HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        log.info("获取私聊会话列表(自动), userId: {}", userId);
        
        // 获取用户的第一个角色
        WgCharacter character = getFirstCharacter(userId);
        if (character == null) {
            return Result.error("未找到角色，请先创建角色");
        }
        
        List<PrivateConversationVO> conversations = chatService.getConversations(character.getId());
        return Result.OK("获取成功", conversations);
    }
    
    /**
     * 获取私聊会话列表（指定角色ID）
     */
    @GetMapping("/conversations/{characterId}")
    public Result<List<PrivateConversationVO>> getConversations(
            HttpServletRequest request,
            @PathVariable String characterId) {
        String userId = getCurrentUserId(request);
        log.info("获取私聊会话列表, userId: {}, characterId: {}", userId, characterId);
        
        // TODO: 验证 characterId 是否属于 userId
        
        List<PrivateConversationVO> conversations = chatService.getConversations(characterId);
        return Result.OK("获取成功", conversations);
    }
    
    /**
     * 获取私聊消息记录（兼容接口，自动获取当前用户的第一个角色）
     */
    @GetMapping("/private/{targetId}")
    public Result<List<ChatMessageVO>> getPrivateMessagesAuto(
            HttpServletRequest request,
            @PathVariable String targetId,
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
        String userId = getCurrentUserId(request);
        log.info("获取私聊消息记录(自动), userId: {}, targetId: {}", userId, targetId);
        
        // 获取用户的第一个角色
        WgCharacter character = getFirstCharacter(userId);
        if (character == null) {
            return Result.error("未找到角色，请先创建角色");
        }
        
        List<ChatMessageVO> messages = chatService.getPrivateMessages(character.getId(), targetId, limit);
        return Result.OK("获取成功", messages);
    }
    
    /**
     * 获取私聊消息记录（指定角色ID）
     */
    @GetMapping("/private/{characterId}/{targetId}")
    public Result<List<ChatMessageVO>> getPrivateMessages(
            HttpServletRequest request,
            @PathVariable String characterId,
            @PathVariable String targetId,
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
        String userId = getCurrentUserId(request);
        log.info("获取私聊消息记录, userId: {}, characterId: {}, targetId: {}", userId, characterId, targetId);
        
        // TODO: 验证 characterId 是否属于 userId
        
        List<ChatMessageVO> messages = chatService.getPrivateMessages(characterId, targetId, limit);
        return Result.OK("获取成功", messages);
    }
    
    /**
     * 发送消息
     */
    @PostMapping("/send")
    public Result<ChatMessageVO> sendMessage(
            HttpServletRequest request,
            @Validated @RequestBody SendMessageDTO dto) {
        String userId = getCurrentUserId(request);
        
        // 从 DTO 中获取 senderId，如果没有则自动获取用户的第一个角色
        String senderId = dto.getSenderId();
        if (senderId == null || senderId.isEmpty()) {
            log.info("发送消息未指定senderId，自动获取用户第一个角色, userId: {}", userId);
            WgCharacter character = getFirstCharacter(userId);
            if (character == null) {
                return Result.error("未找到角色，请先创建角色或在请求中指定senderId");
            }
            senderId = character.getId();
        }
        
        log.info("发送消息, userId: {}, senderId: {}, channel: {}", userId, senderId, dto.getChannel());
        
        // TODO: 验证 senderId 是否属于 userId
        
        ChatMessageVO message = chatService.sendMessage(senderId, dto);
        return Result.OK("发送成功", message);
    }
    
    /**
     * 标记会话为已读（兼容接口，自动获取当前用户的第一个角色）
     */
    @PostMapping("/read/{targetId}")
    public Result<Void> markAsReadAuto(
            HttpServletRequest request,
            @PathVariable String targetId) {
        String userId = getCurrentUserId(request);
        log.info("标记会话为已读(自动), userId: {}, targetId: {}", userId, targetId);
        
        // 获取用户的第一个角色
        WgCharacter character = getFirstCharacter(userId);
        if (character == null) {
            return Result.error("未找到角色，请先创建角色");
        }
        
        chatService.markAsRead(character.getId(), targetId);
        return Result.OK("操作成功", null);
    }
    
    /**
     * 标记会话为已读（指定角色ID）
     */
    @PostMapping("/read/{characterId}/{targetId}")
    public Result<Void> markAsRead(
            HttpServletRequest request,
            @PathVariable String characterId,
            @PathVariable String targetId) {
        String userId = getCurrentUserId(request);
        log.info("标记会话为已读, userId: {}, characterId: {}, targetId: {}", userId, characterId, targetId);
        
        // TODO: 验证 characterId 是否属于 userId
        
        chatService.markAsRead(characterId, targetId);
        return Result.OK("操作成功", null);
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 获取用户的第一个角色
     */
    private WgCharacter getFirstCharacter(String userId) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<WgCharacter> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(WgCharacter::getUserId, userId)
               .eq(WgCharacter::getDelFlag, 0)
               .orderByDesc(WgCharacter::getCreateTime)
               .last("LIMIT 1");
        return characterMapper.selectOne(wrapper);
    }
}
