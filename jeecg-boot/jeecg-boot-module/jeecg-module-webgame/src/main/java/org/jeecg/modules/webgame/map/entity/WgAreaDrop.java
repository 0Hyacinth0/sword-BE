package org.jeecg.modules.webgame.map.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 区域掉落配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
@TableName("wg_area_drops")
@EqualsAndHashCode(callSuper = false)
public class WgAreaDrop implements Serializable {
    private static final long serialVersionUID = 1L;

    /**自增ID*/
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**所属区域ID*/
    private String areaId;
    
    /**物品ID（关联wg_item_template）*/
    private Integer itemId;
    
    /**稀有度(Rare/Epic/Legendary)*/
    private String rarity;
    
    /**基础掉落率*/
    private BigDecimal dropRate;
}
