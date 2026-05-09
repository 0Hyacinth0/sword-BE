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
 * @Description: 装备模板表
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Data
@TableName("wg_equipment_template")
@EqualsAndHashCode(callSuper = false)
public class WgEquipmentTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**装备模板ID*/
    @TableId(type = IdType.INPUT)
    private String equipmentId;
    
    /**装备名称*/
    private String name;
    
    /**槽位类型(weapon/helmet/chest/legs/accessory1/accessory2)*/
    private String slotType;
    
    /**稀有度(Normal/Rare/Epic/Legendary)*/
    private String rarity;
    
    /**基础属性(JSON格式)*/
    private String baseStats;
    
    /**套装ID*/
    private String setId;
    
    /**套装名称*/
    private String setName;
    
    /**装备描述*/
    private String description;
    
    /**图标URL*/
    private String iconUrl;
    
    /**等级需求*/
    private Integer levelRequirement;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
