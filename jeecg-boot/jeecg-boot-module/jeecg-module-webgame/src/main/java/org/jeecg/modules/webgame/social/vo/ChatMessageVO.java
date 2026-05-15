package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

/**
 * @Description: 聊天消息 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class ChatMessageVO {
    
    /**消息ID*/
    private String id;
    
    /**发送者角色ID*/
    private String senderId;
    
    /**发送者角色名*/
    private String senderName;
    
    /**发送者职业*/
    private String senderProfession;
    
    /**频道类型*/
    private String channel;
    
    /**私聊目标ID*/
    private String targetId;
    
    /**私聊目标名称*/
    private String targetName;
    
    /**消息内容*/
    private String content;
    
    /**发送时间*/
    private String timestamp;
}
