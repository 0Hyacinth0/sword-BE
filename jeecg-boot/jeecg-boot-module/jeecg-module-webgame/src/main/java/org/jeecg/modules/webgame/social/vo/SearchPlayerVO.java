package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

/**
 * @Description: 搜索结果 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class SearchPlayerVO {
    
    /**角色ID*/
    private String characterId;
    
    /**角色名*/
    private String characterName;
    
    /**职业*/
    private String profession;
    
    /**等级*/
    private Integer level;
    
    /**是否已是好友*/
    private Boolean isFriend;
    
    /**是否已发送请求*/
    private Boolean hasPendingRequest;
}
