package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

/**
 * @Description: 好友信息 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class FriendInfoVO {
    
    /**好友角色ID*/
    private String characterId;
    
    /**角色名*/
    private String characterName;
    
    /**职业*/
    private String profession;
    
    /**等级*/
    private Integer level;
    
    /**在线状态(online/offline/busy)*/
    private String status;
    
    /**最后在线时间*/
    private String lastOnlineAt;
    
    /**好友关系建立时间*/
    private String addedAt;
}
