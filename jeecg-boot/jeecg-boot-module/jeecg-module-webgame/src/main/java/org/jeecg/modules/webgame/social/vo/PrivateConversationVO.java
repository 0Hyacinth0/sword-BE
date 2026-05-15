package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

/**
 * @Description: 私聊会话 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class PrivateConversationVO {
    
    /**对方角色ID*/
    private String targetId;
    
    /**对方角色名*/
    private String targetName;
    
    /**对方职业*/
    private String targetProfession;
    
    /**最后一条消息*/
    private String lastMessage;
    
    /**最后消息时间*/
    private String lastTime;
    
    /**未读消息数*/
    private Integer unreadCount;
}
