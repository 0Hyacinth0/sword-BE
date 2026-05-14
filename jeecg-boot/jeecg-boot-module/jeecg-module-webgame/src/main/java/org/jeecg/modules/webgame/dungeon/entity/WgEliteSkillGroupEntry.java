package org.jeecg.modules.webgame.dungeon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 精英技能组条目表
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
@TableName("wg_elite_skill_group_entries")
@EqualsAndHashCode(callSuper = false)
public class WgEliteSkillGroupEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    /**自增ID*/
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**技能组ID*/
    private Long groupId;
    
    /**技能ID*/
    private Integer skillId;
    
    /**AI使用优先级*/
    private Integer priority;
}
