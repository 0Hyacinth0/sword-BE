package org.jeecg.modules.webgame.dungeon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 掉落条目表
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
@TableName("wg_drop_entries")
@EqualsAndHashCode(callSuper = false)
public class WgDropEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    /**自增主键*/
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**所属副本ID*/
    private String dungeonId;
    
    /**楼层类型（floor/boss）*/
    private String floorType;
    
    /**物品ID*/
    private Integer itemId;
    
    /**基础掉落率*/
    private BigDecimal dropRate;
    
    /**最小数量*/
    private Integer minQuantity;
    
    /**最大数量*/
    private Integer maxQuantity;
    
    /**是否为精英副本专属掉落*/
    private Boolean eliteOnly;
}
