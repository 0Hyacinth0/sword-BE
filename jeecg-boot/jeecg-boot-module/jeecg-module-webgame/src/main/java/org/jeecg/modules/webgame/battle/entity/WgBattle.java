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
 * @Description: 战斗实例表
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
@TableName("wg_battle")
@EqualsAndHashCode(callSuper = false)
public class WgBattle implements Serializable {
    private static final long serialVersionUID = 1L;

    /**战斗实例唯一ID(UUID)*/
    @TableId(type = IdType.ASSIGN_ID)
    private String battleId;
    
    /**角色ID*/
    private String characterId;
    
    /**出战战宠ID(可选)*/
    private String activePetId;
    
    /**副本ID(可选)*/
    private String dungeonId;
    
    /**关卡ID(可选)*/
    private String stageId;
    
    /**怪物组ID(可选)*/
    private String enemyGroupId;
    
    /**战斗状态(in_progress/completed/abandoned)*/
    private String status;
    
    /**战斗结果(victory/defeat/fled)*/
    private String outcome;
    
    /**当前回合数*/
    private Integer currentRound;
    
    /**战斗阶段*/
    private String battlePhase;
    
    /**玩家造成的总伤害*/
    private Integer totalDamageDealt;
    
    /**玩家承受的总伤害*/
    private Integer totalDamageTaken;
    
    /**玩家治疗总量*/
    private Integer totalHealed;
    
    /**暴击次数*/
    private Integer criticalHits;
    
    /**闪避次数*/
    private Integer dodgeCount;
    
    /**击杀敌人数*/
    private Integer enemiesKilled;
    
    /**完整战斗状态快照(JSON)*/
    private String battleStateJson;
    
    /**战斗开始时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    
    /**战斗结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    
    /**最后行动时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastActionTime;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
