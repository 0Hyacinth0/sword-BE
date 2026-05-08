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
 * @Description: 角色背包表
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Data
@TableName("character_inventory")
@EqualsAndHashCode(callSuper = false)
public class CharacterInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**角色ID*/
    private String characterId;
    
    /**物品模板ID*/
    private Integer itemId;
    
    /**持有数量（≥1）*/
    private Integer quantity;
    
    /**获取时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date obtainedAt;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
