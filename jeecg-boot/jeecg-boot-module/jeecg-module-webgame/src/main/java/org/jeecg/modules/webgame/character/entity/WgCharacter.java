package org.jeecg.modules.webgame.character.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 游戏角色表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_character")
@EqualsAndHashCode(callSuper = false)
public class WgCharacter implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**用户ID*/
    private String userId;
    
    /**角色名称*/
    private String characterName;
    
    /**职业类型(1-战士,2-法师,3-猎人)*/
    private Integer profession;
    
    /**等级*/
    private Integer level;
    
    /**经验值*/
    private Long experience;
    
    /**力量*/
    private Integer strength;
    
    /**智力*/
    private Integer intelligence;
    
    /**敏捷*/
    private Integer agility;
    
    /**生命值*/
    private Integer hp;
    
    /**魔法值*/
    private Integer mp;
    
    /**物理攻击力*/
    private Integer physicalAttack;
    
    /**魔法攻击力*/
    private Integer magicAttack;
    
    /**防御力*/
    private Integer defense;
    
    /**闪避率*/
    private Double dodgeRate;
    
    /**暴击率*/
    private Double criticalRate;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    /**删除状态(0-正常,1-已删除)*/
    private Integer delFlag;
}
