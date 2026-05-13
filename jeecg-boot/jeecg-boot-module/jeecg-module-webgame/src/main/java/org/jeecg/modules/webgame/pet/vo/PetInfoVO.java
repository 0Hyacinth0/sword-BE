package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 战宠信息VO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class PetInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**战宠实例ID*/
    private String id;

    /**战宠类型ID*/
    private Integer petTypeId;

    /**战宠名称（从类型配置获取）*/
    private String name;

    /**战宠昵称*/
    private String nickname;

    /**等级*/
    private Integer level;

    /**当前经验值*/
    private Integer exp;

    /**升级所需经验值*/
    private Integer maxExp;

    /**稀有度(1-N,2-R,3-SR,4-SSR)*/
    private Integer rarity;

    /**是否出战*/
    private Boolean isActive;

    /**元素类型(1-火,2-水,3-风,4-地,5-光,6-暗)*/
    private Integer element;

    /**属性信息*/
    private PetStatsVO stats;

    /**给主人的属性加成*/
    private PetOwnerBonusVO bonusToOwner;

    /**已装备的技能列表（最多3个）*/
    private List<PetSkillVO> skills;

    /**已学会的全部技能列表*/
    private List<PetSkillVO> learnedSkills;

    /**装备信息*/
    private PetEquipmentVO equipment;

    /**进化目标类型ID*/
    private Integer evolveTo;

    /**进化所需等级*/
    private Integer evolveLevel;

    /**战宠描述*/
    private String description;
}
