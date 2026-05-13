package org.jeecg.modules.webgame.pet.entity;

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
 * @Description: 角色战宠实例表
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
@TableName("wg_character_pets")
@EqualsAndHashCode(callSuper = false)
public class CharacterPet implements Serializable {
    private static final long serialVersionUID = 1L;

    /**战宠实例唯一标识(UUID)*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**角色ID*/
    private String characterId;
    
    /**战宠类型ID*/
    private Integer petTypeId;
    
    /**战宠昵称*/
    private String nickname;
    
    /**等级*/
    private Integer level;
    
    /**当前经验值*/
    private Integer exp;
    
    /**升级所需经验值*/
    private Integer maxExp;
    
    /**稀有度(1-N,2-R,3-SR,4-SSR)*/
    private Integer rarity;
    
    /**是否出战(0-否,1-是)*/
    private Integer isActive;
    
    /**技能槽位1(关联wg_pet_skills.skill_id)*/
    @TableField("skill_slot_1")
    private Integer skillSlot1;
    
    /**技能槽位2(关联wg_pet_skills.skill_id)*/
    @TableField("skill_slot_2")
    private Integer skillSlot2;
    
    /**技能槽位3(关联wg_pet_skills.skill_id)*/
    @TableField("skill_slot_3")
    private Integer skillSlot3;
    
    /**护甲装备ID(关联wg_character_items.id)*/
    private String equipArmor;
    
    /**饰品装备ID(关联wg_character_items.id)*/
    private String equipAccessory;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
