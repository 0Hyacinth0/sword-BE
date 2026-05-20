package org.jeecg.modules.webgame.pvp.vo;

import lombok.Data;

/**
 * @Description: PVP匹配对手信息VO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class PvpMatchOpponentVO {

    /** 角色ID */
    private String characterId;

    /** 角色名称 */
    private String characterName;

    /** 职业 */
    private String profession;

    /** 等级 */
    private Integer level;

    /** 段位 */
    private String tier;

    /** 小级 */
    private String subTier;

    /** 积分 */
    private Integer score;
}
