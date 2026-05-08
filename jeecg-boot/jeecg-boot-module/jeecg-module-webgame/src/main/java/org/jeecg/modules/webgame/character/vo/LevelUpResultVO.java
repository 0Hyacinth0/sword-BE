package org.jeecg.modules.webgame.character.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 升级结果VO
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelUpResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**升级前等级*/
    private Integer oldLevel;
    
    /**升级后等级*/
    private Integer newLevel;
    
    /**升级数*/
    private Integer levelsGained;
    
    /**获得的属性点*/
    private Integer pointsGained;
    
    /**溢出经验（新等级内的当前经验）*/
    private Integer overflowExp;
    
    /**是否达到满级*/
    private Boolean isNewMaxLevel;
}
