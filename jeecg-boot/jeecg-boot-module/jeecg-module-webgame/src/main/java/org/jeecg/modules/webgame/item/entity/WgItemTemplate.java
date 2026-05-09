package org.jeecg.modules.webgame.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 物品模板表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_item_template")
@EqualsAndHashCode(callSuper = false)
public class WgItemTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**物品模板ID（varchar 36）*/
    @TableId(type = IdType.INPUT)
    private String itemId;
    
    /**物品名称*/
    private String name;
    
    /**物品类型*/
    private Integer type;
    
    /**装备部位类型*/
    private String slotType;
    
    /**稀有度*/
    private String rarity;
    
    /**基础属性加成（JSON格式）*/
    private String baseStats;
    
    /**套装ID*/
    private String setId;
    
    /**套装名称*/
    private String setName;
    
    /**图标URL*/
    private String icon;
    
    /**物品描述*/
    private String description;
    
    /**等级需求*/
    private Integer levelRequirement;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
