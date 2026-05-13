package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 战宠给主人的属性加成VO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class PetOwnerBonusVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**生命值加成*/
    private Integer hp;

    /**攻击力加成*/
    private Integer attack;

    /**防御力加成*/
    private Integer defense;

    /**暴击率加成(小数形式，如0.05=5%)*/
    private Double criticalRate;

    /**闪避率加成(小数形式，如0.03=3%)*/
    private Double dodgeRate;
}
