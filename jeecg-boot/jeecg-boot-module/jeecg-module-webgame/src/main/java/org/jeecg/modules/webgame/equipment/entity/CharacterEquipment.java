package org.jeecg.modules.webgame.equipment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 角色装备表
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Data
@TableName("wg_character_equipments")
@EqualsAndHashCode(callSuper = false)
public class CharacterEquipment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**装备实例唯一标识*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**角色ID*/
    private String characterId;
    
    /**装备模板ID*/
    private String itemId;
    
    /**槽位类型(weapon/helmet/chest/legs/accessory1/accessory2)*/
    private String slotType;
    
    /**随机词条（JSON格式，额外属性）*/
    private String extraStats;
    
    /**强化等级*/
    private Integer enhanceLevel;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
