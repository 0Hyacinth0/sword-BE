package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

/**
 * @Description: 战宠给主人的属性加成VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class PetBonusToMasterVO {

    /** HP加成值 */
    private Integer maxHp;

    /** 物理攻击加成值 */
    private Integer physicalAttack;

    /** 魔法攻击加成值 */
    private Integer magicAttack;

    /** 防御加成值 */
    private Integer defense;

    /** 闪避率加成值 */
    private Double dodgeRate;

    /** 暴击率加成值 */
    private Double criticalRate;
}
