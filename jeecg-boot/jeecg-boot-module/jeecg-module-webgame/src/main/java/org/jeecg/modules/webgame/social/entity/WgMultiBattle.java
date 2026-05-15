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
 * @Description: 多人战斗实例表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_multi_battles")
@EqualsAndHashCode(callSuper = false)
public class WgMultiBattle implements Serializable {
    private static final long serialVersionUID = 1L;

    /**战斗实例ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String battleId;
    
    /**副本房间ID*/
    @TableField("room_id")
    private String roomId;
    
    /**副本ID*/
    @TableField("dungeon_id")
    private String dungeonId;
    
    /**当前楼层*/
    private Integer floor;
    
    /**总楼层数*/
    private Integer totalFloors;
    
    /**战斗状态(in_progress/completed/failed)*/
    private String status;
    
    /**怪物属性缩放倍率*/
    private BigDecimal monsterScale;
    
    /**当前回合数*/
    private Integer currentRound;
    
    /**完整战斗状态快照(JSON)*/
    private String battleStateJson;
    
    /**战斗开始时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    
    /**战斗结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
