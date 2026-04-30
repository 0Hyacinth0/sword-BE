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
 * @Description: 玩家背包表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_player_item")
@EqualsAndHashCode(callSuper = false)
public class WgPlayerItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**用户ID*/
    private String userId;
    
    /**物品模板ID*/
    private String itemTemplateId;
    
    /**物品名称(冗余字段,方便查询)*/
    private String itemName;
    
    /**数量*/
    private Integer quantity;
    
    /**是否装备中(0-未装备,1-已装备)*/
    private Integer isEquipped;
    
    /**强化等级*/
    private Integer enhanceLevel;
    
    /**附加属性JSON*/
    private String affixes;
    
    /**套装ID*/
    private String setId;
    
    /**获得时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date obtainTime;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
