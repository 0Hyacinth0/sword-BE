package org.jeecg.modules.webgame.map.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 区域怪物配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
@TableName("wg_area_monsters")
@EqualsAndHashCode(callSuper = false)
public class WgAreaMonster implements Serializable {
    private static final long serialVersionUID = 1L;

    /**怪物配置ID*/
    @TableId
    private String id;
    
    /**所属区域ID*/
    private String areaId;
    
    /**关联怪物模板ID（可选）*/
    private String monsterTemplateId;
    
    /**怪物名称*/
    private String name;
    
    /**怪物等级*/
    private Integer level;
    
    /**怪物类型(normal/elite/boss)*/
    private String type;
    
    /**排序顺序*/
    private Integer sortOrder;
}
