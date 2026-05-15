package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

/**
 * @Description: 入队申请 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class TeamApplicationVO {
    
    /**申请ID*/
    private String id;
    
    /**队伍ID*/
    private String teamId;
    
    /**申请者角色ID*/
    private String applicantId;
    
    /**申请者角色名*/
    private String applicantName;
    
    /**申请者职业*/
    private String applicantProfession;
    
    /**申请者等级*/
    private Integer applicantLevel;
    
    /**申请状态*/
    private String status;
    
    /**创建时间*/
    private String createdAt;
}
