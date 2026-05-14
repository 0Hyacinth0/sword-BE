package org.jeecg.modules.webgame.battle.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 等级配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
@TableName("wg_level_config")
@EqualsAndHashCode(callSuper = false)
public class WgLevelConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**等级*/
    @TableId
    private Integer level;
    
    /**升级所需总经验值*/
    private Long expRequired;
    
    /**升到下一级所需经验*/
    private Long expToNext;
    
    /**升级获得的属性点*/
    private Integer statPoints;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
