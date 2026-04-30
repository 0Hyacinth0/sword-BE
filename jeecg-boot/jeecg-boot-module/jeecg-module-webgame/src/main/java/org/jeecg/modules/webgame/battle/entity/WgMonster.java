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
 * @Description: 怪物表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_monster")
@EqualsAndHashCode(callSuper = false)
public class WgMonster implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**怪物名称*/
    private String monsterName;
    
    /**所属区域*/
    private String area;
    
    /**最低等级*/
    private Integer minLevel;
    
    /**最高等级*/
    private Integer maxLevel;
    
    /**生命值*/
    private Integer hp;
    
    /**物理攻击力*/
    private Integer physicalAttack;
    
    /**魔法攻击力*/
    private Integer magicAttack;
    
    /**防御力*/
    private Integer defense;
    
    /**经验值奖励*/
    private Integer expReward;
    
    /**金币奖励*/
    private Integer goldReward;
    
    /**掉落物品ID列表(JSON格式)*/
    private String dropItems;
    
    /**是否BOSS(0-普通,1-BOSS)*/
    private Integer isBoss;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
