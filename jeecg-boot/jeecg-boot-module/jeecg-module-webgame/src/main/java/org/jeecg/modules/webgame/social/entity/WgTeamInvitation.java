package org.jeecg.modules.webgame.social.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 队伍邀请表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_team_invitations")
@EqualsAndHashCode(callSuper = false)
public class WgTeamInvitation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**队伍ID*/
    @TableField("team_id")
    private String teamId;
    
    /**邀请者角色ID*/
    @TableField("inviter_id")
    private String inviterId;
    
    /**被邀请者角色ID*/
    @TableField("invitee_id")
    private String inviteeId;
    
    /**邀请状态(pending/accepted/rejected)*/
    private String status;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
