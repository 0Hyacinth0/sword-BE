package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description: 队伍信息 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class TeamInfoVO {
    
    /**队伍ID*/
    private String id;
    
    /**队长角色ID*/
    private String leaderId;
    
    /**队长角色名*/
    private String leaderName;
    
    /**成员列表*/
    private List<TeamMemberVO> members;
    
    /**最大人数*/
    private Integer maxMembers;
    
    /**队伍状态*/
    private String status;
    
    /**目标副本*/
    private String targetDungeon;
    
    /**创建时间*/
    private String createdAt;
}
