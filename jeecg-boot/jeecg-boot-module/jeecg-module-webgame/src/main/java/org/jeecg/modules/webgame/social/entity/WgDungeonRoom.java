package org.jeecg.modules.webgame.social.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 副本房间表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_dungeon_rooms")
@EqualsAndHashCode(callSuper = false)
public class WgDungeonRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**关联队伍ID*/
    @TableField("team_id")
    private String teamId;
    
    /**副本配置ID*/
    @TableField("dungeon_id")
    private String dungeonId;
    
    /**队长角色ID*/
    @TableField("leader_id")
    private String leaderId;
    
    /**房间状态(waiting/ready/starting/in_progress/completed/cancelled)*/
    private String status;
    
    /**怪物缩放倍率*/
    private BigDecimal monsterScale;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
