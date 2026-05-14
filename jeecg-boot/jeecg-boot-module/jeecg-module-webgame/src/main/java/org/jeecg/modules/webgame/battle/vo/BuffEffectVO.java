package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

/**
 * @Description: Buff效果VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class BuffEffectVO {

    private String uid;

    private String name;

    private Boolean isDebuff;

    private String stat;

    private Double value;

    private Integer duration;

    private Integer remainingTurns;

    private Integer sourceSkillId;
}
