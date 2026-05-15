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
 * @Description: Boss配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_boss_configs")
@EqualsAndHashCode(callSuper = false)
public class WgBossConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**Boss唯一标识*/
    @TableField("boss_id")
    private String bossId;
    
    /**Boss名称*/
    private String bossName;
    
    /**阶段配置JSON数组*/
    private String phasesJson;
    
    /**狂暴配置JSON*/
    private String enrageJson;
    
    /**复活配置JSON*/
    private String reviveJson;
    
    /**AOE技能ID列表JSON*/
    private String aoeSkillIds;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
