package org.jeecg.modules.webgame.pet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 战宠技能配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
@TableName("wg_pet_skills")
@EqualsAndHashCode(callSuper = false)
public class PetSkill implements Serializable {
    private static final long serialVersionUID = 1L;

    /**技能ID*/
    @TableId(type = IdType.AUTO)
    private Integer skillId;
    
    /**技能名称*/
    private String name;
    
    /**技能类型(active_attack/active_support/passive)*/
    private String type;
    
    /**技能威力(百分比,如120表示120%)*/
    private Integer power;
    
    /**冷却时间(回合数)*/
    private Integer cooldown;
    
    /**学习等级(0表示进化专属技能)*/
    private Integer learnLevel;
    
    /**技能描述*/
    private String description;
    
    /**技能图标URL*/
    private String iconUrl;
    
    /**可学习该技能的战宠类型ID列表(JSON数组)*/
    private String petTypeIds;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
