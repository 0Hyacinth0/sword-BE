package org.jeecg.modules.webgame.map.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 区域掉落DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
public class AreaDropDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**物品ID*/
    private Integer itemId;
    
    /**物品名称*/
    private String name;
    
    /**稀有度(Rare/Epic/Legendary)*/
    private String rarity;
}
