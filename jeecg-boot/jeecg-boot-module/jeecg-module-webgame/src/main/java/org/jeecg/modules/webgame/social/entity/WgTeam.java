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
 * @Description: 队伍表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_teams")
@EqualsAndHashCode(callSuper = false)
public class WgTeam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**队长角色ID*/
    @TableField("leader_id")
    private String leaderId;
    
    /**最大人数（默认4）*/
    private Integer maxMembers;
    
    /**队伍状态(open/closed/in_dungeon)*/
    private String status;
    
    /**目标副本标识*/
    private String targetDungeon;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
