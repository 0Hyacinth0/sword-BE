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
 * @Description: 副本表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_dungeon")
@EqualsAndHashCode(callSuper = false)
public class WgDungeon implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**副本名称*/
    private String dungeonName;
    
    /**副本类型(1-单人,2-多人)*/
    private Integer dungeonType;
    
    /**最低等级要求*/
    private Integer minLevel;
    
    /**推荐战斗力*/
    private Integer recommendedPower;
    
    /**最大人数*/
    private Integer maxPlayers;
    
    /**体力消耗*/
    private Integer staminaCost;
    
    /**怪物列表(JSON格式)*/
    private String monsters;
    
    /**奖励物品列表(JSON格式)*/
    private String rewards;
    
    /**副本描述*/
    private String description;
    
    /**是否启用(0-禁用,1-启用)*/
    private Integer isEnabled;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
