package org.jeecg.modules.webgame.dungeon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 精英技能组表
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
@TableName("wg_elite_skill_groups")
@EqualsAndHashCode(callSuper = false)
public class WgEliteSkillGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    /**技能组ID*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**技能组名称*/
    private String name;
}
