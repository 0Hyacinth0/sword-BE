package org.jeecg.modules.webgame.dungeon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 副本房间表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_dungeon_room")
@EqualsAndHashCode(callSuper = false)
public class WgDungeonRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**副本ID*/
    private String dungeonId;
    
    /**房主用户ID*/
    private String hostUserId;
    
    /**房间名称*/
    private String roomName;
    
    /**当前人数*/
    private Integer currentPlayers;
    
    /**最大人数*/
    private Integer maxPlayers;
    
    /**房间状态(1-等待中,2-进行中,3-已结束)*/
    private Integer status;
    
    /**玩家列表(JSON格式)*/
    private String players;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**开始时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    
    /**结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
