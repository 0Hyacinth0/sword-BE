package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 战宠属性VO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class PetStatsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**当前生命值*/
    private Integer hp;

    /**最大生命值*/
    private Integer maxHp;

    /**攻击力*/
    private Integer attack;

    /**防御力*/
    private Integer defense;

    /**速度*/
    private Integer speed;
}
