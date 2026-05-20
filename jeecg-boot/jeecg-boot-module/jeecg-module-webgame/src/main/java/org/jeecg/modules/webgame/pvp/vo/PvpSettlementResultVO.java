package org.jeecg.modules.webgame.pvp.vo;

import lombok.Data;

/**
 * @Description: PVP战斗结算结果VO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class PvpSettlementResultVO {

    /** 积分变化 */
    private Integer scoreChange;

    /** 原积分 */
    private Integer oldScore;

    /** 新积分 */
    private Integer newScore;

    /** 段位是否变化 */
    private Boolean tierChanged;

    /** 原段位信息 */
    private TierInfoVO oldTier;

    /** 新段位信息 */
    private TierInfoVO newTier;

    /**
     * 段位信息内部类
     */
    @Data
    public static class TierInfoVO {
        /** 段位 */
        private String tier;

        /** 小级 */
        private String subTier;

        /** 段位名称 */
        private String tierName;

        /** 进度百分比 */
        private Double progress;

        /** 距离下一段位剩余积分 */
        private Integer remainingScore;
    }
}
