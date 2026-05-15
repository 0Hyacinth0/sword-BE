package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

/**
 * @Description: 好友请求 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class FriendRequestVO {
    
    /**请求ID*/
    private String id;
    
    /**发送者角色ID*/
    private String fromCharacterId;
    
    /**发送者角色名*/
    private String fromCharacterName;
    
    /**发送者职业*/
    private String fromProfession;
    
    /**发送者等级*/
    private Integer fromLevel;
    
    /**接收者角色ID*/
    private String toCharacterId;
    
    /**请求状态*/
    private String status;
    
    /**创建时间*/
    private String createdAt;
}
