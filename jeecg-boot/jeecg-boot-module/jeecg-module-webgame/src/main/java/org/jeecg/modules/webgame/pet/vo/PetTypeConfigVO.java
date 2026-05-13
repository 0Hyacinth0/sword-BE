package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 战宠类型配置VO（图鉴用）
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class PetTypeConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**战宠类型ID*/
    private Integer petTypeId;

    /**战宠名称*/
    private String name;

    /**元素类型(1-火,2-水,3-风,4-地,5-光,6-暗)*/
    private Integer element;

    /**稀有度(1-N,2-R,3-SR,4-SSR)*/
    private Integer rarity;

    /**基础生命值*/
    private Integer baseHp;

    /**基础攻击力*/
    private Integer baseAttack;

    /**基础防御力*/
    private Integer baseDefense;

    /**基础速度*/
    private Integer baseSpeed;

    /**战宠描述*/
    private String description;

    /**进化目标类型ID*/
    private Integer evolveTo;

    /**进化所需等级*/
    private Integer evolveLevel;

    /**可学习的技能列表*/
    private List<PetSkillVO> skills;
}
