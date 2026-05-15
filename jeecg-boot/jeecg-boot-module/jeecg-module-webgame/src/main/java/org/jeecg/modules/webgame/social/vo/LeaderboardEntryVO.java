package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

/**
 * @Description: 排行榜条目 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class LeaderboardEntryVO {
    
    /**排名*/
    private Integer rank;
    
    /**角色ID*/
    private String characterId;
    
    /**角色名*/
    private String characterName;
    
    /**职业*/
    private String profession;
    
    /**等级*/
    private Integer level;
    
    /**排序值（等级/战力/竞技积分）*/
    private Object value;
    
    /**是否在线*/
    private Boolean isOnline;
}
