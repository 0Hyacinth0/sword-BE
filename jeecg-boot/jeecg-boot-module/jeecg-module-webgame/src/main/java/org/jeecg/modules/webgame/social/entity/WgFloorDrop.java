package org.jeecg.modules.webgame.social.entity;

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
 * @Description: 楼层掉落记录表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_floor_drops")
@EqualsAndHashCode(callSuper = false)
public class WgFloorDrop implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**战斗实例ID*/
    @TableField("battle_id")
    private String battleId;
    
    /**角色ID*/
    @TableField("character_id")
    private String characterId;
    
    /**楼层*/
    private Integer floor;
    
    /**物品ID*/
    private Integer itemId;
    
    /**物品名称*/
    private String itemName;
    
    /**数量*/
    private Integer quantity;
    
    /**品质(common/rare/epic/legendary)*/
    private String quality;
    
    /**掉落时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
