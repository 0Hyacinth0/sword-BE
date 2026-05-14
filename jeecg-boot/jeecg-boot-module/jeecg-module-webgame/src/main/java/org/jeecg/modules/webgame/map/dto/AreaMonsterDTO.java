package org.jeecg.modules.webgame.map.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 区域怪物DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
public class AreaMonsterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**怪物ID*/
    private String id;
    
    /**怪物名称*/
    private String name;
    
    /**怪物等级*/
    private Integer level;
    
    /**怪物类型(normal/elite/boss)*/
    private String type;
}
