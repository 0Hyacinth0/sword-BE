package org.jeecg.modules.webgame.battle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 战斗记录表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_battle_log")
@EqualsAndHashCode(callSuper = false)
public class WgBattleLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**用户ID*/
    private String userId;
    
    /**角色ID*/
    private String characterId;
    
    /**战斗类型(1-PVE,2-PVP,3-副本)*/
    private Integer battleType;
    
    /**怪物ID(PVE时使用)*/
    private String monsterId;
    
    /**对手玩家ID(PVP时使用)*/
    private String opponentId;
    
    /**副本ID(副本时使用)*/
    private String dungeonId;
    
    /**战斗结果(1-胜利,2-失败,3-平局)*/
    private Integer result;
    
    /**获得经验值*/
    private Integer expGained;
    
    /**获得金币*/
    private Integer goldGained;
    
    /**掉落物品ID列表(JSON格式)*/
    private String dropItems;
    
    /**战斗回合数*/
    private Integer rounds;
    
    /**战斗详情JSON*/
    private String battleDetail;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
