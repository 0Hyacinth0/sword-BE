package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

/**
 * @Description: 队伍成员 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class TeamMemberVO {
    
    /**角色ID*/
    private String characterId;
    
    /**角色名*/
    private String characterName;
    
    /**职业*/
    private String profession;
    
    /**等级*/
    private Integer level;
    
    /**成员角色(leader/member)*/
    private String role;
    
    /**在线状态*/
    private String status;
    
    /**加入时间*/
    private String joinedAt;
}
