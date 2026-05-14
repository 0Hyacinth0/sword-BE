package org.jeecg.modules.webgame.battle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 怪物掉落配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
@TableName("wg_monster_drops")
@EqualsAndHashCode(callSuper = false)
public class WgMonsterDrop implements Serializable {
    private static final long serialVersionUID = 1L;

    /**掉落配置ID*/
    @TableId(type = IdType.AUTO)
    private Integer dropId;
    
    /**怪物模板ID*/
    private String monsterId;
    
    /**物品ID*/
    private String itemId;
    
    /**最小掉落数量*/
    private Integer minQuantity;
    
    /**最大掉落数量*/
    private Integer maxQuantity;
    
    /**掉落权重*/
    private Integer dropWeight;
    
    /**物品品质*/
    private String quality;
    
    /**物品类型*/
    private String itemType;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
