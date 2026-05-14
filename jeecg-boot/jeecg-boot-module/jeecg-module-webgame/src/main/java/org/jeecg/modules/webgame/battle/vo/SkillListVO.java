package org.jeecg.modules.webgame.battle.vo;

import lombok.Data;

/**
 * @Description: 技能列表响应VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class SkillListVO {

    /** 主动技能列表 */
    private java.util.List<BattleSkillVO> activeSkills;

    /** 被动技能列表 */
    private java.util.List<BattleSkillVO> passiveSkills;
}
