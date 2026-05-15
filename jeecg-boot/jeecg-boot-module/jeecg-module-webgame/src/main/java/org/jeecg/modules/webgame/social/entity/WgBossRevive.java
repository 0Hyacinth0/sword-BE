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
 * @Description: 复活记录表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_boss_revives")
@EqualsAndHashCode(callSuper = false)
public class WgBossRevive implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**战斗实例ID*/
    @TableField("battle_id")
    private String battleId;
    
    /**被复活角色ID*/
    @TableField("revived_character_id")
    private String revivedCharacterId;
    
    /**复活者角色ID*/
    @TableField("reviver_character_id")
    private String reviverCharacterId;
    
    /**复活时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviveTime;
}
