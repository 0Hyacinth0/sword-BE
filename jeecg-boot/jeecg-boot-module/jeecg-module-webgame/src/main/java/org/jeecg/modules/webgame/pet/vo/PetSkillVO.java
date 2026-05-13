package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 战宠技能VO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class PetSkillVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**技能ID*/
    private Integer id;

    /**技能名称*/
    private String name;

    /**技能类型(active_attack/active_support/passive)*/
    private String type;

    /**技能威力(百分比)*/
    private Integer power;

    /**冷却时间(回合数)*/
    private Integer cooldown;

    /**学习等级*/
    private Integer learnLevel;

    /**技能描述*/
    private String description;
}
