package org.jeecg.modules.webgame.pvp.vo;

import lombok.Data;

/**
 * @Description: 玩家竞技数据VO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class ArenaPlayerVO {

    /** 段位 */
    private String tier;

    /** 小级 */
    private String subTier;

    /** 当前积分 */
    private Integer score;

    /** 胜利场次 */
    private Integer wins;

    /** 失败场次 */
    private Integer losses;

    /** 胜率 */
    private Double winRate;

    /** 赛季ID */
    private String seasonId;
}
